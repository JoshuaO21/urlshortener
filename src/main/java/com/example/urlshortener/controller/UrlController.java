package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class UrlController {

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        String code = service.shortenUrl(url);
        return ResponseEntity.ok(Map.of("short_url", "http://localhost:8080/" + code));
    }

    @GetMapping("/{code}")
    public RedirectView redirect(@PathVariable String code) { // Search for original URL using the code, if not found,
                                                              // redirect to a 404 page
        String originalUrl = service.getOriginalUrl(code);
        if (originalUrl == null) {
            return new RedirectView("/not-found");
        }
        return new RedirectView(originalUrl);
    }

    @GetMapping("/not-found")
    public ResponseEntity<String> notFound() {
        return ResponseEntity.status(404).body("Shortened URL Not Found");
    }
}