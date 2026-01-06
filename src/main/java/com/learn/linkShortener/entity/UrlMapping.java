package com.learn.linkShortener.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "url_mapping")
@Data
@NoArgsConstructor
public class UrlMapping {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    private LocalDateTime createdAt;

    private Long hitCount;
}
