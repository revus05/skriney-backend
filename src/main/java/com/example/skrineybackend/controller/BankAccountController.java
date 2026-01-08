package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.service.BankAccountService;
import com.example.skrineybackend.swagger.bankaccount.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
@Tag(name = "Счета", description = "Управление счетами пользователей")
@RequiredArgsConstructor
public class BankAccountController {
  private final BankAccountService bankAccountService;

  @GetAllBankAccountsOperation
  @GetMapping()
  public Response getAllBankAccounts() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<BankAccountDTO> bankAccounts =
        bankAccountService.getAllBankAccounts(((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счета пользователя успешно получены", HttpStatus.OK, bankAccounts);
  }

  @GetOneBankAccountOperation
  @GetMapping("{uuid}")
  public Response getOneBankAccount(@PathVariable String uuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO bankAccount =
        bankAccountService.getOneBankAccount(
            ((UserDetails) auth.getPrincipal()).getUsername(), uuid);
    return new Response("Счет пользователя успешно получен", HttpStatus.OK, bankAccount);
  }

  @CreateBankAccountOperation
  @PostMapping()
  public Response createBankAccount(@Valid @RequestBody CreateBankAccountRequestDTO requestBody) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO createdBankAccount =
        bankAccountService.createBankAccount(
            requestBody, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счет успешно создан", HttpStatus.CREATED, createdBankAccount);
  }

  @DeleteBankAccountOperation
  @DeleteMapping("{uuid}")
  public Response deleteBankAccount(@PathVariable String uuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO deletedBankAccount =
        bankAccountService.deleteBankAccount(
            uuid, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счет успешно удален", HttpStatus.OK, deletedBankAccount);
  }

  @UpdateBankAccountOperation
  @PatchMapping("{uuid}")
  public Response updateBankAccount(
      @PathVariable String uuid,
      @Valid @RequestBody UpdateBankAccountRequestDTO updateBankAccountRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO updatedBankAccount =
        bankAccountService.updateBankAccount(
            uuid, updateBankAccountRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счет пользователя успешно обновлен", HttpStatus.OK, updatedBankAccount);
  }
}
