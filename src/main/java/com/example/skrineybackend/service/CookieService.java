package com.example.skrineybackend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void addJwtCookie(HttpServletResponse response, String token) {
        String cookie = String.format(
                "jwt=%s; Path=/; Max-Age=%d; Secure; SameSite=None",
                token,
                86400
        );
        response.addHeader("Set-Cookie", cookie);
    }
}
