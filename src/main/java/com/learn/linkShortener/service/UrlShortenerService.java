package com.learn.linkShortener.service;

import com.learn.linkShortener.entity.UrlMapping;

public interface UrlShortenerService {
	    
    UrlMapping createOrGetShortUrl(String originalUrl);
    
    String resolveShortUrl(String shortCode);
    
    String updateShortUrlStatus(String shortCode, Boolean enabled);
    
    void updateShortUrlStatusv1(String shortCode, Boolean enabled);
}
