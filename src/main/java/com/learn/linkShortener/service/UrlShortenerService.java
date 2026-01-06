package com.learn.linkShortener.service;

public interface UrlShortenerService {
	
	String shortenUrl(String originalUrl);

    String getOriginalUrl(String shortCode);
}
