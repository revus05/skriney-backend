package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.*;
import com.example.skrineybackend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

    @Operation(
        summary = "Создание транзакций",
        description = "Создает транзакцию",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания транзакции",
            content = @Content(
                schema = @Schema(implementation = CreateTransactionRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = """
                        {\
                          "description": "please",\
                          "sum": 63.05,\
                          "bankAccountUuid": "42043119-7ee4-4106-aa92-afa7e49b092b",
                          "categoryUuid": "4f95009e-43fd-4cf4-86d2-cbba4626d35d"
                        }"""
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Транзакция успешно создана",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class))
            ),
        }
    )
    @PostMapping("/create")
    public Response createTransaction(@Valid @RequestBody CreateTransactionRequestDTO createTransactionRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TransactionDTO createdTransaction = transactionService.createTransaction(createTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакция успешно создана", HttpStatus.CREATED, createdTransaction);
    }

    @Operation(
        summary = "Получение транзакций со всех счетов",
        description = "Получение всех счетов транзакций пользователя",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Транзакции получены успешно",
                content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class))
                )
            ),
        }
    )
    @GetMapping()
    public Response getTransactions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TransactionDTO> transactions = transactionService.getTransactions(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакции успешно получены", HttpStatus.OK, transactions);
    }

    @Operation(
        summary = "Удаление транзакции",
        description = "Удаляет транзакцию пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для удаления транзакции",
            content = @Content(
                schema = @Schema(implementation = DeleteTransactionRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"uuid\":\"bcbfe240-a61b-4e21-94f0-188bddf4a04e\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Транзакция успешно удалена",
                content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            ),
        }
    )
    @DeleteMapping("/delete")
    public Response deleteTransaction(@Valid @RequestBody DeleteTransactionRequestDTO deleteTransactionRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TransactionDTO deletedTransaction = transactionService.deleteTransaction(deleteTransactionRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Транзакция успешно удалена", HttpStatus.OK, deletedTransaction);
    }
}
