package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.CreateAccountRequestDTO;
import com.example.skrineybackend.exeption.NoUserFoundException;
import com.example.skrineybackend.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequestDTO requestBody) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok().body(accountService.createAccount(requestBody, ((UserDetails) auth.getPrincipal()).getUsername()));
        } catch (NoUserFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Неизвестная ошибка");
        }
    }
}
