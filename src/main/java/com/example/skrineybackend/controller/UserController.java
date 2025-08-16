package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.*;
import com.example.skrineybackend.service.CookieService;
import com.example.skrineybackend.service.JwtService;
import com.example.skrineybackend.service.UserService;
import com.example.skrineybackend.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserSettingsService userSettingsService;

    public UserController(UserService userService, JwtService jwtService, CookieService cookieService, UserSettingsService userSettingsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.userSettingsService = userSettingsService;
    }

    @Operation(
        summary = "Регистрация пользователя",
        description = "Создает нового пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для регистрации пользователя",
            content = @Content(
                schema = @Schema(implementation = SignUpUserRequestDTO.class),
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
    @PostMapping("/sign-up")
    public Response signUpUser(@Valid @RequestBody SignUpUserRequestDTO signUpUserRequestDTO) {
        return new Response("Пользователь успешно создан", HttpStatus.CREATED, userService.signUpUser(signUpUserRequestDTO));
    }

    @Operation(
        summary = "Авторизация пользователя",
        description = "Авторизует пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для авторизации пользователя",
            content = @Content(
                schema = @Schema(implementation = SignInUserRequestDTO.class),
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
    @PostMapping("/sign-in")
    public Response signInUser(@Valid @RequestBody SignInUserRequestDTO signInUserRequestDTO, HttpServletResponse response) {
        UserDTO loggedUser = userService.signInUser(signInUserRequestDTO);
        String token = jwtService.generateToken(loggedUser.getUuid());
        cookieService.addJwtCookie(response, token);
        return new Response("Пользователь успешно авторизован", HttpStatus.OK, loggedUser);
    }

    @Operation(
        summary = "Авторизация пользователя по jwt",
        description = "Авторизует пользователя по jwt",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Пользователь успешно авторизован",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
        }
    )
    @GetMapping("/me")
    public Response getMeWithJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO loggedUser = userService.getMe(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Пользователь успешно авторизован", HttpStatus.OK, loggedUser);
    }

    @Operation(
        summary = "Обновление валюты по умолчанию",
        description = "Обновляет валюту по умолчанию у пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новая валюта по умолчанию",
            content = @Content(
                schema = @Schema(implementation = UpdateDefaultCurrencyRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"currency\": \"BYN\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Валюта по умолчанию успешно обновлена",
                content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))
            ),
        }
    )
    @PostMapping("/update-default-currency")
    public Response updateDefaultCurrency(@Valid @RequestBody UpdateDefaultCurrencyRequestDTO updateDefaultCurrencyRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserSettingsDTO updatedUserSettings = userSettingsService.updateDefaultCurrency(updateDefaultCurrencyRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Валюта по умолчанию успешно обновлена",  HttpStatus.OK, updatedUserSettings);
    }

    @Operation(
        summary = "Обновление категории по умолчанию",
        description = "Обновляет категорию по умолчанию у пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новая категория по умолчанию",
            content = @Content(
                schema = @Schema(implementation = UpdateDefaultCategoryRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"uuid\": \"9be5a6db-e01f-4a9b-8243-4b1fe9f37213\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Категория по умолчанию успешно обновлена",
                content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))
            ),
        }
    )
    @PostMapping("/update-default-category")
    public Response updateDefaultCategory(@Valid @RequestBody UpdateDefaultCategoryRequestDTO updateDefaultCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserSettingsDTO updatedUserSettings = userSettingsService.updateDefaultCategory(updateDefaultCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория по умолчанию успешно обновлена",  HttpStatus.OK, updatedUserSettings);
    }

    @Operation(
        summary = "Получение настроек пользователя",
        description = "Возвращает настройки пользователя",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Настройки пользователя успешно получены",
                content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))
            ),
        }
    )
    @GetMapping("/settings")
    public Response getUserSettings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserSettingsDTO updatedUserSettings = userSettingsService.getUserSettings(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Настройки пользователя успешно получены",  HttpStatus.OK, updatedUserSettings);
    }
}