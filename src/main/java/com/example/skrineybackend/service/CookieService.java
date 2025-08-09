package com.example.skrineybackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void addJwtCookie(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400);

        response.addCookie(jwtCookie);
    }
}
