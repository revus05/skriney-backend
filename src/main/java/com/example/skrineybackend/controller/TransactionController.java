package com.example.skrineybackend.controller;

import com.example.skrineybackend.applicationservice.TransactionApplicationService;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.dto.transaction.UpdateTransactionRequestDTO;
import com.example.skrineybackend.swagger.transaction.CreateTransactionOperation;
import com.example.skrineybackend.swagger.transaction.DeleteTransactionOperation;
import com.example.skrineybackend.swagger.transaction.GetTransactionsOperation;
import com.example.skrineybackend.swagger.transaction.UpdateTransactionOperation;
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
@RequestMapping("/transactions")
@Tag(name = "Транзакции", description = "Управление транзакциями")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionApplicationService transactionApplicationService;

  @CreateTransactionOperation
  @PostMapping()
  public Response createTransaction(
      @Valid @RequestBody CreateTransactionRequestDTO createTransactionRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    TransactionDTO createdTransaction =
        transactionApplicationService.createTransaction(
            createTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Транзакция успешно создана", HttpStatus.CREATED, createdTransaction);
  }

  @GetTransactionsOperation
  @GetMapping()
  public Response getTransactions() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<TransactionDTO> transactions =
        transactionApplicationService.getAllUserTransactions(
            ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Транзакции успешно получены", HttpStatus.OK, transactions);
  }

  @DeleteTransactionOperation
  @DeleteMapping("{uuid}")
  public Response deleteTransaction(@PathVariable String uuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    TransactionDTO deletedTransaction =
        transactionApplicationService.deleteTransaction(
            uuid, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Транзакция успешно удалена", HttpStatus.OK, deletedTransaction);
  }

  @UpdateTransactionOperation
  @PatchMapping("{uuid}")
  public Response updateTransaction(
      @PathVariable String uuid,
      @Valid @RequestBody UpdateTransactionRequestDTO updateTransactionRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    TransactionDTO updatedTransaction =
        transactionApplicationService.updateTransaction(
            uuid, updateTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Транзакция успешно обновлена", HttpStatus.OK, updatedTransaction);
  }
}
