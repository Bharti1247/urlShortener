package com.learn.linkShortener.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learn.linkShortener.dto.ShortUrlStatusRequest;
import com.learn.linkShortener.dto.ShortUrlStatusResponse;
import com.learn.linkShortener.entity.UrlMapping;
import com.learn.linkShortener.service.UrlShortenerServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerServiceImpl service;

    // Create or get short URL
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
    
    // Redirect to original URL, if enabled
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String originalUrl = service.resolveShortUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, originalUrl)
                .build();
    }
    
    // Enable/Disable short URL
    @PatchMapping("/{shortCode}/status")
//    public ResponseEntity<Void> updateStatus(
//    		@PathVariable String shortCode, @Valid @RequestBody ShortUrlStatusRequest request) {
//
//        service.updateShortUrlStatus(shortCode, request.getEnabled());
//        return ResponseEntity.noContent().build();
//    }
    public ResponseEntity<ShortUrlStatusResponse> updateStatus(
            @PathVariable String shortCode, @Valid @RequestBody ShortUrlStatusRequest request) {

        String message = service.updateShortUrlStatus(shortCode, request.getEnabled());

        return ResponseEntity.ok(new ShortUrlStatusResponse(message));
    }
    
}
