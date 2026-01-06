package com.learn.linkShortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learn.linkShortener.entity.UrlMapping;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);
    
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
}
