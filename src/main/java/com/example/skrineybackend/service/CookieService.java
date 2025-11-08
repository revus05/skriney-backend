package com.example.skrineybackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Service
public class CookieService {
    @Value("${cors.origin}")
    private String CORS_ORIGIN;

    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;

    public void addJwtCookie(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("jwt", token);

        jwtCookie.setSecure(Objects.equals(ACTIVE_PROFILE, "production"));
        jwtCookie.setHttpOnly(true);

        String sameSite = Objects.equals(ACTIVE_PROFILE, "production") ? "None" : "Lax";
        jwtCookie.setAttribute("SameSite", sameSite);

        String domain = extractDomain(CORS_ORIGIN);
        if (domain != null) {
            jwtCookie.setDomain(domain);
        }

        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24 * 7);

        response.addCookie(jwtCookie);
    }

    private String extractDomain(String origin) {
        try {
            URI uri = new URI(origin);
            return uri.getHost();
        } catch (URISyntaxException e) {
            System.err.println("Invalid CORS_ORIGIN: " + origin);
        }
        return null;
    }
}
