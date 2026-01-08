package com.example.skrineybackend.swagger.transaction;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
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
    summary = "Создание транзакций",
    description = "Создает транзакцию",
    requestBody =
        @RequestBody(
            description = "Данные для создания транзакции",
            content =
                @Content(
                    schema = @Schema(implementation = CreateTransactionRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "description": "and the next day",
                      "sum": -4.05,
                      "bankAccountUuid": "123123",
                      "categoryUuid": "1b6d5bc2-cb68-4404-ab7e-df1b78099c18"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Транзакция успешно создана",
          content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
    })
public @interface CreateTransactionOperation {}
