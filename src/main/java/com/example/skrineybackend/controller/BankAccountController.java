package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.BankAccountDTO;
import com.example.skrineybackend.dto.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.Response;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.service.BankAccountService;
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
@RequestMapping("/bank-accounts")
@Tag(name = "Счета", description = "Управление счетами пользователей")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(
        summary = "Создание счета",
        description = "Создает счет для пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания счета",
            content = @Content(
                schema = @Schema(implementation = CreateBankAccountRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"title\":\"bank account name\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Счет успешно создан",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
        }
    )
    @PostMapping("/create")
    public Response createBankAccount(@Valid @RequestBody CreateBankAccountRequestDTO requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BankAccountDTO createdBankAccount = bankAccountService.createBankAccount(requestBody, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Счет успешно создан", HttpStatus.CREATED, createdBankAccount);
    }
}
