package com.learn.linkShortener.entity;

import java.time.LocalDateTime;

import com.learn.linkShortener.enums.AuditAction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "url_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortCode;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    private String performedBy;

    private Boolean previousState;

    private Boolean newState;

    private LocalDateTime performedAt;
}

