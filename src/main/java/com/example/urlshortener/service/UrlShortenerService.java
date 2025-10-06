package com.example.urlshortener.service;

import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class UrlShortenerService {

    @Autowired // Dependency Injection to make things easier
    private ShortUrlRepository repository;

    public String shortenUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5"); // hash function
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            String code = HexFormat.of().formatHex(hash).substring(0, 6);

            ShortUrl shortUrl = new ShortUrl();
            shortUrl.setCode(code);
            shortUrl.setOriginalUrl(originalUrl); // Assign original URL to entity
            repository.save(shortUrl);

            return code;
        } catch (Exception e) {
            throw new RuntimeException("Error generating short URL", e);
        }
    }

    public String getOriginalUrl(String code) {
        return repository.findByCode(code)
                .map(ShortUrl::getOriginalUrl)
                .orElse(null);
    }
}