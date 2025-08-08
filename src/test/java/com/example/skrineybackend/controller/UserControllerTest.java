package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.SignUpUserRequest;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void signUp_User_ValidRequest_ReturnsSuccessResponse() throws Exception {
        SignUpUserRequest requestBody = new SignUpUserRequest(
            "testusername",
            "testemail@gmail.com",
            "testpassword"
        );

        String requestBodyJsonAuto = new ObjectMapper().writeValueAsString(requestBody);

        UserDTO mockUser = new UserDTO(new User(requestBody));
        when(userService.signUpUser(any(SignUpUserRequest.class))).thenReturn(mockUser);

        mockMvc.perform(post("/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyJsonAuto))
            .andExpect(status().isCreated());
    }
}
