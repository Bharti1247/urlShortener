package com.learn.linkShortener.exceptions;

@SuppressWarnings("serial")
public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
