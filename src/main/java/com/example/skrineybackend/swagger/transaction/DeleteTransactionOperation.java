package com.example.skrineybackend.swagger.transaction;

import com.example.skrineybackend.dto.transaction.DeleteTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Удаление транзакции",
    description = "Удаляет транзакцию пользователя",
    requestBody = @RequestBody(
        description = "Данные для удаления транзакции",
        content = @Content(
            schema = @Schema(implementation = DeleteTransactionRequestDTO.class),
            examples = @ExampleObject(
                name = "Пример запроса",
                value = """
                    {
                      "uuid": "9be5a6db-e01f-4a9b-8243-4b1fe9f37213"
                    }"""
            )
        )
    ),
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Транзакция успешно удалена",
            content = @Content(schema = @Schema(implementation = TransactionDTO.class))
        ),
    }
)
public @interface DeleteTransactionOperation {
}
