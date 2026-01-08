package com.example.skrineybackend;

import static com.example.skrineybackend.config.JwtAuthenticationFilter.JWT_COOKIE_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

  @Autowired protected MockMvc mockMvc;

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    PostgreSQLContainer<?> postgres = TestContainerSingleton.getInstance();
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  public Cookie getAuthCookie() throws Exception {
    SignUpUserRequestDTO signUpBody =
        new SignUpUserRequestDTO("testusername", "testemail@gmail.com", "testpassword");

    mockMvc.perform(
        post("/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(signUpBody)));

    MvcResult signInResult =
        mockMvc
            .perform(
                post("/users/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"testemail@gmail.com\",\"password\": \"testpassword\"}"))
            .andReturn();

    Cookie[] cookies = signInResult.getResponse().getCookies();
    Cookie authCookie = null;
    for (Cookie cookie : cookies) {
      if (JWT_COOKIE_NAME.equals(cookie.getName())) {
        authCookie = cookie;
        break;
      }
    }
    assertThat(authCookie).isNotNull();

    return authCookie;
  }
}
