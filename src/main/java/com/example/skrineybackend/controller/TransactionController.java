package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.DeleteTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.service.TransactionService;
import com.example.skrineybackend.swagger.transaction.CreateTransactionOperation;
import com.example.skrineybackend.swagger.transaction.DeleteTransactionOperation;
import com.example.skrineybackend.swagger.transaction.GetTransactionsOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Транзакции", description = "Управление транзакциями")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @CreateTransactionOperation
    @PostMapping("/create")
    public Response createTransaction(@Valid @RequestBody CreateTransactionRequestDTO createTransactionRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TransactionDTO createdTransaction = transactionService.createTransaction(createTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакция успешно создана", HttpStatus.CREATED, createdTransaction);
    }

    @GetTransactionsOperation
    @GetMapping()
    public Response getTransactions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TransactionDTO> transactions = transactionService.getTransactions(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакции успешно получены", HttpStatus.OK, transactions);
    }

    @DeleteTransactionOperation
    @DeleteMapping("/delete")
    public Response deleteTransaction(@Valid @RequestBody DeleteTransactionRequestDTO deleteTransactionRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TransactionDTO deletedTransaction = transactionService.deleteTransaction(deleteTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакция успешно удалена", HttpStatus.OK, deletedTransaction);
    }
}
