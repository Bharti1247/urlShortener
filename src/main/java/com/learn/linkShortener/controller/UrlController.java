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
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerServiceImpl service;

    // Create or get short URL
    @PostMapping("/api/createOrGetShortUrl")
    public ResponseEntity<String> shorten(@RequestBody Map<String, String> body) {

        String originalUrl = body.get("url");

        log.info("Received request to create/get short URL");

        if (originalUrl == null || originalUrl.isBlank()) {
            log.warn("Invalid request: originalUrl is missing or blank");
            return ResponseEntity.badRequest().body("URL must not be empty");
        }

        UrlMapping mapping = service.createOrGetShortUrl(originalUrl);

        String shortUrl = "http://localhost:8080/" + mapping.getShortCode();

        log.info("Short URL generated/retrieved successfully shortCode={}",
                 mapping.getShortCode());

        return ResponseEntity.ok(shortUrl);
    }

    // Redirect to original URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        log.info("Redirect request received shortCode={}", shortCode);

        String originalUrl = service.resolveShortUrl(shortCode);

        log.info("Redirecting shortCode={} to originalUrl={}",
                 shortCode, originalUrl);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, originalUrl)
                .build();
    }

    // Enable / Disable short URL
    @PatchMapping("/{shortCode}/status")
    public ResponseEntity<ShortUrlStatusResponse> updateStatus(
            @PathVariable String shortCode,
            @Valid @RequestBody ShortUrlStatusRequest request) {

        log.info("Update short URL status request received shortCode={} enabled={}",
                 shortCode, request.getEnabled());

        String message = service.updateShortUrlStatus(shortCode, request.getEnabled());

        log.info("Short URL status updated successfully shortCode={} enabled={}",
                 shortCode, request.getEnabled());

        return ResponseEntity.ok(new ShortUrlStatusResponse(message));
    }
}
