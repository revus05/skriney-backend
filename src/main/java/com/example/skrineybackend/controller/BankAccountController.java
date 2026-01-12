package com.example.skrineybackend.controller;

import com.example.skrineybackend.applicationservice.BankAccountApplicationService;
import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.swagger.bankaccount.CreateBankAccountOperation;
import com.example.skrineybackend.swagger.bankaccount.DeleteBankAccountOperation;
import com.example.skrineybackend.swagger.bankaccount.GetAllBankAccountsOperation;
import com.example.skrineybackend.swagger.bankaccount.UpdateBankAccountOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-accounts")
@Tag(name = "Счета", description = "Управление счетами пользователей")
@RequiredArgsConstructor
public class BankAccountController {
  private final BankAccountApplicationService bankAccountApplicationService;

  @GetAllBankAccountsOperation
  @GetMapping()
  public Response getAllBankAccounts() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<BankAccountDTO> bankAccounts =
        bankAccountApplicationService.getAllBankAccounts(
            ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счета пользователя успешно получены", HttpStatus.OK, bankAccounts);
  }

  @CreateBankAccountOperation
  @PostMapping()
  public Response createBankAccount(@Valid @RequestBody CreateBankAccountRequestDTO requestBody) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO createdBankAccount =
        bankAccountApplicationService.createBankAccount(
            ((UserDetails) auth.getPrincipal()).getUsername(), requestBody);
    return new Response("Счет успешно создан", HttpStatus.CREATED, createdBankAccount);
  }

  @DeleteBankAccountOperation
  @DeleteMapping("{uuid}")
  public Response deleteBankAccount(@PathVariable String uuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    BankAccountDTO deletedBankAccount =
        bankAccountApplicationService.deleteBankAccount(
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
        bankAccountApplicationService.updateBankAccount(
            uuid, updateBankAccountRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Счет пользователя успешно обновлен", HttpStatus.OK, updatedBankAccount);
  }
}
