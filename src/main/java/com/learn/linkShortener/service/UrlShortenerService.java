package com.learn.linkShortener.service;

import com.learn.linkShortener.entity.UrlMapping;

public interface UrlShortenerService {
	
	String shortenUrl(String originalUrl);

    String getOriginalUrl(String shortCode);
    
    UrlMapping createOrGetShortUrl(String originalUrl);
    
    String resolveShortUrl(String shortCode);
    
    void enableShortUrl(String shortCode);
}
