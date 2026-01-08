package com.example.skrineybackend.config;

import com.example.skrineybackend.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String JWT_COOKIE_NAME = "jwt";

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = extractToken(request);

    if (token != null && jwtService.validateToken(token)) {
      Authentication auth = jwtService.getAuthentication(token);

      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String cookieString = request.getHeader("Cookie");
    if (cookieString == null) {
      return null;
    }
    String[] cookies = cookieString.split("; ");
    for (String cookie : cookies) {
      if (cookie.contains(JWT_COOKIE_NAME + "=")) {
        return cookie.substring((JWT_COOKIE_NAME + "=").length());
      }
    }

    return null;
  }
}
