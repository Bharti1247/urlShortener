package com.learn.linkShortener.exceptions;

@SuppressWarnings("serial")
public class ShortUrlDisabledException extends RuntimeException {
    public ShortUrlDisabledException(String message) {
        super(message);
    }
}
