package com.learn.linkShortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learn.linkShortener.entity.UrlAudit;

public interface AuditRepository extends JpaRepository<UrlAudit, Long>{

}
