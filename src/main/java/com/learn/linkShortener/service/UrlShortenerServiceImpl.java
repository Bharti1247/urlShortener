package com.learn.linkShortener.service;

import java.time.LocalDateTime;
import java.util.UUID;

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

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {
	
	private final UrlRepository urlRepository;
    private final AuditRepository auditRepository;

	@Override
    public UrlMapping createOrGetShortUrl(String originalUrl) {
		        
		return urlRepository.findByOriginalUrl(originalUrl)
				.orElseGet(() -> {
                    UrlMapping mapping = new UrlMapping();
                    mapping.setOriginalUrl(originalUrl);
                    mapping.setShortCode(generateShortCode());
                    mapping.setCreatedAt(LocalDateTime.now());
                    mapping.setHitCount(0L);
                    return urlRepository.save(mapping);
                });
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
    
    @Override
    @Transactional
    public String resolveShortUrl(String shortCode) {

        UrlMapping mapping = urlRepository
                .findByShortCodeAndShortUrlEnabledTrue(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL does not exist"));
        
        if (!mapping.getShortUrlEnabled()) {
            throw new ShortUrlDisabledException("This short URL has been disabled");
        }
        
        mapping.setHitCount(mapping.getHitCount() + 1);

        return mapping.getOriginalUrl();
    }
    
    @Override
    @Transactional
    public String updateShortUrlStatus(String shortCode, Boolean enabled) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL does not exist"));
        
        Boolean oldState = mapping.getShortUrlEnabled();
        
        if (enabled.equals(oldState)) {
            return enabled ? "Short URL is already enabled" : "Short URL is already disabled";
        }

        mapping.setShortUrlEnabled(enabled);
        
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

        return enabled ? "Short URL has been enabled successfully" : "Short URL has been disabled successfully";
    }

}
