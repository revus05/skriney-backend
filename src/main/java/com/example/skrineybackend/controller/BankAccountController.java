package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.DeleteBankAccountRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.service.BankAccountService;
import com.example.skrineybackend.swagger.bankaccount.CreateBankAccountOperation;
import com.example.skrineybackend.swagger.bankaccount.DeleteBankAccountOperation;
import com.example.skrineybackend.swagger.bankaccount.GetBankAccountOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-accounts")
@Tag(name = "Счета", description = "Управление счетами пользователей")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @CreateBankAccountOperation
    @PostMapping("/create")
    public Response createBankAccount(@Valid @RequestBody CreateBankAccountRequestDTO requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BankAccountDTO createdBankAccount = bankAccountService.createBankAccount(requestBody, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Счет успешно создан", HttpStatus.CREATED, createdBankAccount);
    }

    @DeleteBankAccountOperation
    @DeleteMapping("/delete")
    public Response deleteBankAccount(@Valid @RequestBody DeleteBankAccountRequestDTO requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BankAccountDTO deletedBankAccount = bankAccountService.deleteBankAccount(requestBody, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Счет успешно удален", HttpStatus.OK, deletedBankAccount);
    }

    @GetBankAccountOperation
    @GetMapping()
    public Response getBankAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BankAccountDTO> bankAccounts = bankAccountService.getBankAccounts(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Счета пользователя успешно получены", HttpStatus.OK, bankAccounts);
    }
}
