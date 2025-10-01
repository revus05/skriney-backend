package com.example.skrineybackend.controller;

import com.example.skrineybackend.AbstractIntegrationTest;
import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class UserControllerTest extends AbstractIntegrationTest {
    @Test
    void signUp_User_ValidRequest_ReturnsSuccessResponse() throws Exception {
        SignUpUserRequestDTO requestBody = new SignUpUserRequestDTO(
                "testusername",
                "testemail@gmail.com",
                "testpassword"
        );

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.username").value("testusername"))
                .andExpect(jsonPath("$.data.email").value("testemail@gmail.com"));
    }

    @Test
    void signUp_User_UserWithUsernameExists_ReturnsErrorResponse() throws Exception {
        SignUpUserRequestDTO requestBody1 = new SignUpUserRequestDTO(
                "testusername",
                "testemail@gmail.com",
                "testpassword"
        );SignUpUserRequestDTO requestBody2 = new SignUpUserRequestDTO(
                "testusername",
                "testemail123@gmail.com",
                "testpassword"
        );

        String requestBodyJson1 = new ObjectMapper().writeValueAsString(requestBody1);
        String requestBodyJson2 = new ObjectMapper().writeValueAsString(requestBody2);

        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson1));

        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Запись с такими данными уже существует"))
                .andExpect(jsonPath("$.data.username").value("Имя пользователя занято"));
    }

    @Test
    void signUp_User_UserWithEmailExists_ReturnsErrorResponse() throws Exception {
        SignUpUserRequestDTO requestBody1 = new SignUpUserRequestDTO(
                "testusername",
                "testemail@gmail.com",
                "testpassword"
        );SignUpUserRequestDTO requestBody2 = new SignUpUserRequestDTO(
                "testusername123",
                "testemail@gmail.com",
                "testpassword"
        );

        String requestBodyJson1 = new ObjectMapper().writeValueAsString(requestBody1);
        String requestBodyJson2 = new ObjectMapper().writeValueAsString(requestBody2);

        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson1));

        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Запись с такими данными уже существует"))
                .andExpect(jsonPath("$.data.email").value("Пользователь с таким email уже существует"));
    }
}