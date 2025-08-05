package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.RegisterRequestDTO;
import com.example.skrineybackend.dto.Response;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.service.CookieService;
import com.example.skrineybackend.service.JwtService;
import com.example.skrineybackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public UserController(UserService userService, JwtService jwtService, CookieService cookieService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @Operation(
        summary = "Регистрация пользователя",
        description = "Создает нового пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для регистрации пользователя",
            content = @Content(
                schema = @Schema(implementation = RegisterRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"username\":\"john_doe\",\"password\":\"securePassword123\",\"email\":\"john@example.com\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Пользователь успешно создан",
                content = @Content(
                    schema = @Schema(implementation = UserDTO.class)
                )
            )
        }
    )
    @PostMapping("/register")
    public Response register(@Valid @RequestBody RegisterRequestDTO user) {
        return new Response("Пользователь успешно создан", HttpStatus.CREATED, userService.registerUser(user));
    }

    @Operation(
        summary = "Авторизация пользователя",
        description = "Авторизирует пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для авторизации пользователя",
            content = @Content(
                schema = @Schema(implementation = LoginRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"email\":\"john@example.com\",\"password\":\"securePassword123\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пользователь успешно авторизован",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
        }
    )
    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        UserDTO loggedUser = userService.loginUser(loginRequestDTO);
        String token = jwtService.generateToken(loggedUser.getUuid());
        cookieService.addJwtCookie(response, token);
        return new Response("Пользователь успешно авторизован", HttpStatus.OK, loggedUser);
    }
}