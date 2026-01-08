package com.example.skrineybackend.controller;

import static com.example.skrineybackend.config.JwtAuthenticationFilter.JWT_COOKIE_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.skrineybackend.AbstractIntegrationTest;
import com.example.skrineybackend.dto.user.SignInUserRequestDTO;
import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
class UserControllerTest extends AbstractIntegrationTest {
  @Test
  void signUp_User_ValidRequest_ReturnsSuccessResponse() throws Exception {
    SignUpUserRequestDTO requestBody =
        new SignUpUserRequestDTO("testusername", "testemail@gmail.com", "testpassword");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    mockMvc
        .perform(
            post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.data.username").value("testusername"))
        .andExpect(jsonPath("$.data.email").value("testemail@gmail.com"));
  }

  @Test
  void signUp_User_UserWithUsernameExists_ReturnsErrorResponse() throws Exception {
    SignUpUserRequestDTO requestBody1 =
        new SignUpUserRequestDTO("testusername", "testemail@gmail.com", "testpassword");
    SignUpUserRequestDTO requestBody2 =
        new SignUpUserRequestDTO("testusername", "testemail123@gmail.com", "testpassword");

    String requestBodyJson1 = new ObjectMapper().writeValueAsString(requestBody1);
    String requestBodyJson2 = new ObjectMapper().writeValueAsString(requestBody2);

    mockMvc.perform(
        post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson1));

    mockMvc
        .perform(
            post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson2))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
        .andExpect(jsonPath("$.message").value("Запись с такими данными уже существует"))
        .andExpect(jsonPath("$.data.username").value("Имя пользователя занято"));
  }

  @Test
  void signUp_User_UserWithEmailExists_ReturnsErrorResponse() throws Exception {
    SignUpUserRequestDTO requestBody1 =
        new SignUpUserRequestDTO("testusername", "testemail@gmail.com", "testpassword");
    SignUpUserRequestDTO requestBody2 =
        new SignUpUserRequestDTO("testusername123", "testemail@gmail.com", "testpassword");

    String requestBodyJson1 = new ObjectMapper().writeValueAsString(requestBody1);
    String requestBodyJson2 = new ObjectMapper().writeValueAsString(requestBody2);

    mockMvc.perform(
        post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson1));

    mockMvc
        .perform(
            post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson2))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
        .andExpect(jsonPath("$.message").value("Запись с такими данными уже существует"))
        .andExpect(jsonPath("$.data.email").value("Пользователь с таким email уже существует"));
  }

  @Test
  void signIn_User_ValidRequest_ReturnsSuccessResponse() throws Exception {
    SignUpUserRequestDTO signUpBody =
        new SignUpUserRequestDTO("testusername", "testemail@gmail.com", "testpassword");

    mockMvc.perform(
        post("/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(signUpBody)));

    SignInUserRequestDTO requestBody =
        new SignInUserRequestDTO("testemail@gmail.com", "testpassword");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    mockMvc
        .perform(
            post("/users/sign-in").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.data.username").value("testusername"))
        .andExpect(jsonPath("$.data.email").value("testemail@gmail.com"))
        .andExpect(cookie().exists(JWT_COOKIE_NAME));
  }

  @Test
  void signIn_User_NoUser_ReturnsUnauthorizedResponse() throws Exception {
    SignInUserRequestDTO requestBody =
        new SignInUserRequestDTO("testemail@gmail.com", "testpassword");

    String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

    mockMvc
        .perform(
            post("/users/sign-in").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()));
  }

  @Test
  void signIn_User_NoPassword_ReturnsErrorResponse() throws Exception {
    String requestBodyJson = "{\"email\": \"testemail@gmail.com\"}";

    mockMvc
        .perform(
            post("/users/sign-in").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.message").value("Ошибка валидации"))
        .andExpect(jsonPath("$.data.password").value("Пароль обязателен"));
  }

  @Test
  void signIn_User_NoEmail_ReturnsErrorResponse() throws Exception {
    String requestBodyJson = "{\"password\": \"testpassword\"}";

    mockMvc
        .perform(
            post("/users/sign-in").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.message").value("Ошибка валидации"))
        .andExpect(jsonPath("$.data.email").value("Email обязателен"));
  }

  @Test
  void getMe_User_ValidRequest_ReturnsSuccessResponse() throws Exception {
    mockMvc
        .perform(get("/users/me").cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.data.username").value("testusername"))
        .andExpect(jsonPath("$.data.email").value("testemail@gmail.com"));
  }

  @Test
  void getMe_User_InvalidToken_ReturnsErrorResponse() throws Exception {
    Cookie invalidCookie = new Cookie(JWT_COOKIE_NAME, "some-random-value");

    mockMvc.perform(get("/users/me").cookie(invalidCookie)).andExpect(status().isForbidden());
  }

  @Test
  void updateImage_User_InvalidToken_ReturnsErrorResponse() throws Exception {
    mockMvc
        .perform(
            patch("/users/update-image")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"image\": \"some-new-url\"}")
                .cookie(getAuthCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.data.image").value("some-new-url"));
  }
}
