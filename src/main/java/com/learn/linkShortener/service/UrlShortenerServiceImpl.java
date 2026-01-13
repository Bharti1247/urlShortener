package com.learn.linkShortener.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.learn.linkShortener.entity.UrlMapping;
import com.learn.linkShortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {
	
	private final UrlRepository urlRepository;

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
    public String resolveShortUrl(String shortCode) {

        UrlMapping mapping = urlRepository
                .findByShortCodeAndShortUrlEnabledTrue(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "Short URL is disabled"));

        return mapping.getOriginalUrl();
    }
    
    @Override
    @Transactional
    public void updateShortUrlStatusv1(String shortCode, Boolean enabled) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        if (enabled.equals(mapping.getShortUrlEnabled())) {
            return; // idempotent operation
        }

        mapping.setShortUrlEnabled(enabled);
    }
    
    @Override
    @Transactional
    public String updateShortUrlStatus(String shortCode, Boolean enabled) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        if (enabled.equals(mapping.getShortUrlEnabled())) {
            return enabled ? "Short URL is already enabled" : "Short URL is already disabled";
        }

        mapping.setShortUrlEnabled(enabled);

        return enabled ? "Short URL has been enabled successfully" : "Short URL has been disabled successfully";
    }

}
