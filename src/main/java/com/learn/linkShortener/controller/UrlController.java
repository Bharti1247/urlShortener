package com.learn.linkShortener.controller;

//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learn.linkShortener.entity.UrlMapping;
import com.learn.linkShortener.service.UrlShortenerServiceImpl;

import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

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
    
    // Create short URL HTTP 303
    @PostMapping("/api/createOrGetShortUrl")
    public ResponseEntity<String> shorten(@RequestBody Map<String, String> body) {

        String originalUrl = body.get("url");

        Optional<UrlMapping> existing =
        		Optional.of(service.createOrGetShortUrl(originalUrl));

        if (existing.isPresent()) {
        	return ResponseEntity.ok("http://localhost:8080/" + existing.get().getShortCode());
        }

        UrlMapping created = service.createOrGetShortUrl(originalUrl);
        return ResponseEntity.ok("http://localhost:8080/" + created.getShortCode());
    }
}
