package com.learn.linkShortener.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String raw = "ops123";
        String hash = "$2a$10$LlQtDCkq1MmONFpiwGrTkuj3cFck8RO5RPJlOVNdUr5Gma7UVjPv.";

        System.out.println("Matches: " + encoder.matches(raw, hash));
        
        System.out.println(new BCryptPasswordEncoder().encode("ops123"));
    }
}

