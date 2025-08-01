package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.RegisterRequestDTO;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.exeption.InvalidCredentialsException;
import com.example.skrineybackend.exeption.InvalidEmailException;
import com.example.skrineybackend.exeption.UserAlreadyExistsException;
import com.example.skrineybackend.service.CookieService;
import com.example.skrineybackend.service.JwtService;
import com.example.skrineybackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                    description = "Успешно"
                ),
                @ApiResponse(responseCode = "400", description = "Ошибка в теле запроса")
            }
    )
    @ApiResponse(responseCode = "200", description = "Регистрация прошла успешно")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO user) {
        try{
            return ResponseEntity.ok().body(userService.registerUser(user));
        } catch (UserAlreadyExistsException e) {
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        try{
            UserDTO loggedUser = userService.loginUser(loginRequestDTO);
            String token = jwtService.generateToken(loggedUser.getEmail());
            cookieService.addJwtCookie(response, token);
            return ResponseEntity.ok().body(loggedUser);

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}