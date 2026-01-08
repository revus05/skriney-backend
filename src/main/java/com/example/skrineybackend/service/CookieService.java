package com.example.skrineybackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
  @Value("${cors.origin}")
  private String CORS_ORIGIN;

  @Value("${spring.profiles.active}")
  private String ACTIVE_PROFILE;

  public void createJwtCookie(HttpServletResponse response, String value, int maxAge) {
    Cookie jwtCookie = new Cookie("jwt", value);

    jwtCookie.setSecure(Objects.equals(ACTIVE_PROFILE, "production"));
    jwtCookie.setHttpOnly(true);

    String sameSite = Objects.equals(ACTIVE_PROFILE, "production") ? "None" : "Lax";
    jwtCookie.setAttribute("SameSite", sameSite);

    String domain = extractDomain(CORS_ORIGIN);
    if (domain != null) {
      jwtCookie.setDomain(domain);
    }

    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(maxAge);

    response.addCookie(jwtCookie);
  }

  private String extractDomain(String origin) {
    try {
      URI uri = new URI(origin);
      String host = uri.getHost();

      if (host == null) {
        return null;
      }

      if (host.startsWith("www.")) {
        host = host.substring(4);
      }

      return host;
    } catch (URISyntaxException e) {
      System.err.println("Invalid CORS_ORIGIN: " + origin);
    }
    return null;
  }
}
