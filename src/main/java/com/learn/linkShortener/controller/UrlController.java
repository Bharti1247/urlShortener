package com.learn.linkShortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.learn.linkShortener.service.UrlShortenerService;
import com.learn.linkShortener.service.UrlShortenerServiceImpl;

import lombok.RequiredArgsConstructor;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerServiceImpl service;

    // Create short URL
    @PostMapping("/api/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String url) {
        String shortCode = service.shortenUrl(url);
        return ResponseEntity.ok("http://localhost:8080/" + shortCode);
    }

    // Redirect to original URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = service.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
