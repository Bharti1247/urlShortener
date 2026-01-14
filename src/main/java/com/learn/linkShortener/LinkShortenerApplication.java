package com.learn.linkShortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LinkShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkShortenerApplication.class, args);
	}

}
