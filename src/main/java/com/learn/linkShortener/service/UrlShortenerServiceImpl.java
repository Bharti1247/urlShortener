package com.learn.linkShortener.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.learn.linkShortener.entity.UrlMapping;
import com.learn.linkShortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {
	
	private final UrlRepository urlRepository;

	@Override
	public String shortenUrl(String originalUrl) {
		String shortCode = generateShortCode();

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(shortCode);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setHitCount(0L);

        urlRepository.save(mapping);
        return shortCode;
	}

	@Override
	public String getOriginalUrl(String shortCode) {
        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        mapping.setHitCount(mapping.getHitCount() + 1);
        urlRepository.save(mapping);

        return mapping.getOriginalUrl();
    }
    
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

}
