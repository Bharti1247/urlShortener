package com.learn.linkShortener.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.linkShortener.entity.UrlAudit;
import com.learn.linkShortener.entity.UrlMapping;
import com.learn.linkShortener.enums.AuditAction;
import com.learn.linkShortener.exceptions.ShortUrlDisabledException;
import com.learn.linkShortener.exceptions.ShortUrlNotFoundException;
import com.learn.linkShortener.repository.AuditRepository;
import com.learn.linkShortener.repository.UrlRepository;
import com.learn.linkShortener.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlRepository urlRepository;
    private final AuditRepository auditRepository;

    //Create a new short URL or return existing mapping.
    @Override
    @CacheEvict(value = "shortUrlCache", key = "#result.shortCode", condition = "#result != null")
    public UrlMapping createOrGetShortUrl(String originalUrl) {

        log.info("Request received to create/get short URL for originalUrl={}", originalUrl);

        UrlMapping mapping = urlRepository.findByOriginalUrl(originalUrl)
                .orElseGet(() -> {
                    log.info("No existing mapping found. Creating new short URL.");

                    UrlMapping newMapping = new UrlMapping();
                    newMapping.setOriginalUrl(originalUrl);
                    newMapping.setShortCode(generateShortCode());
                    newMapping.setCreatedAt(LocalDateTime.now());
                    newMapping.setHitCount(0L);

                    UrlMapping saved = urlRepository.save(newMapping);

                    log.info("Short URL created successfully. shortCode={}", saved.getShortCode());
                    return saved;
                });

        log.debug("Returning mapping shortCode={} for originalUrl={}",
                mapping.getShortCode(), originalUrl);

        return mapping;
    }

    private String generateShortCode() {
        String shortCode = UUID.randomUUID().toString().substring(0, 6);
        log.debug("Generated shortCode={}", shortCode);
        return shortCode;
    }

    //Resolve short URL to original URL. Cached for performance.
    @Override
    @Transactional
    @Cacheable(value = "shortUrlCache", key = "#shortCode", unless = "#result == null")
    public String resolveShortUrl(String shortCode) {

        log.info("Resolving short URL. shortCode={}", shortCode);

        // Indicates DB hit only when cache miss occurs
        log.debug("Cache miss. Fetching from DB for shortCode={}", shortCode);

        UrlMapping mapping = urlRepository
                .findByShortCodeAndShortUrlEnabledTrue(shortCode)
                .orElseThrow(() -> {
                    log.warn("Short URL not found or disabled in DB. shortCode={}", shortCode);
                    return new ShortUrlNotFoundException("Short URL does not exist");
                });

        if (!mapping.getShortUrlEnabled()) {
            log.warn("Short URL is disabled. shortCode={}", shortCode);
            throw new ShortUrlDisabledException("This short URL has been disabled");
        }

        mapping.setHitCount(mapping.getHitCount() + 1);

        log.info("Short URL resolved successfully. shortCode={}, originalUrl={}",
                shortCode, mapping.getOriginalUrl());

        return mapping.getOriginalUrl();
    }

    //Enable or disable a short URL. Cache eviction is required to avoid stale data.
    @Override
    @Transactional
    @CacheEvict(value = "shortUrlCache", key = "#shortCode")
    public String updateShortUrlStatus(String shortCode, Boolean enabled) {

        log.info("Request received to update short URL status. shortCode={}, enabled={}",
                shortCode, enabled);

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> {
                    log.warn("Short URL not found for status update. shortCode={}", shortCode);
                    return new ShortUrlNotFoundException("Short URL does not exist");
                });

        Boolean oldState = mapping.getShortUrlEnabled();

        if (enabled.equals(oldState)) {
            log.info("No status change required. shortCode={}, currentState={}",
                    shortCode, oldState);
            return enabled ? "Short URL is already enabled" : "Short URL is already disabled";
        }

        mapping.setShortUrlEnabled(enabled);

        log.info("Short URL status updated. shortCode={}, oldState={}, newState={}",
                shortCode, oldState, enabled);

        // ---- AUDIT ENTRY ----
        UrlAudit audit = UrlAudit.builder()
                .shortCode(shortCode)
                .action(enabled ? AuditAction.ENABLE : AuditAction.DISABLE)
                .performedBy(SecurityUtil.getCurrentUser())
                .previousState(oldState)
                .newState(enabled)
                .performedAt(LocalDateTime.now())
                .build();

        auditRepository.save(audit);

        log.info("Audit entry created for shortCode={}, action={}, performedBy={}",
                shortCode, audit.getAction(), audit.getPerformedBy());

        return enabled ? "Short URL has been enabled successfully" : "Short URL has been disabled successfully";
    }
}
