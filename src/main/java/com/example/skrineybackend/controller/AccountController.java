package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.*;
import com.example.skrineybackend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Счета", description = "Управление счетами пользователей")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
        summary = "Создание счета",
        description = "Создает счет для пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания счета",
            content = @Content(
                schema = @Schema(implementation = LoginRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"name\":\"account name\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Счет успешно создан",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
        }
    )
    @PostMapping("/create")
    public Response createAccount(@Valid @RequestBody CreateAccountRequestDTO requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO createdAccount = accountService.createAccount(requestBody, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Счет успешно создан", HttpStatus.OK, createdAccount);
    }
}
