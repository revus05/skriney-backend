package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exeption.InvalidCredentialsException;
import com.example.skrineybackend.exeption.InvalidEmailException;
import com.example.skrineybackend.exeption.UserAlreadyExistsException;
import com.example.skrineybackend.service.CookieService;
import com.example.skrineybackend.service.JwtService;
import com.example.skrineybackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "auth_success",
                                            value = "{\"token\": \"abc123\", \"expiresIn\": 3600}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @ApiResponse(responseCode = "200", description = "Регистрация прошла успешно")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try{
            return ResponseEntity.ok().body(userService.registerUser(user));
        } catch (UserAlreadyExistsException | InvalidEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
         catch (Exception e) {
            return ResponseEntity.badRequest().body("Неизвестная ошибка");
        }
    }

    @Operation(
        summary = "Авторизация пользователя",
        description = "Авторизирует пользователя",
        responses = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
    )
    @ApiResponse(responseCode = "200", description = "Авторизация прошла успешно")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        try{
            UserDTO loggedUser = userService.loginUser(loginRequestDTO);
            String token = jwtService.generateToken(loggedUser.getEmail());
            cookieService.addJwtCookie(response, token);
            return ResponseEntity.ok().body(loggedUser);

        } catch (InvalidCredentialsException | InvalidEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
