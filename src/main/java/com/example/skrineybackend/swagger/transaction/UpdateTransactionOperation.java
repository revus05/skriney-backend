package com.example.skrineybackend.swagger.transaction;

import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.dto.transaction.UpdateTransactionRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Обновление транзакции пользователя",
    description = "Обновляет данные существующей транзакции пользователя",
    parameters = {
      @Parameter(
          name = "transactionUuid",
          description = "UUID транзакции, которую нужно обновить",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(type = "string", format = "uuid"))
    },
    requestBody =
        @RequestBody(
            description = "Данные для обновления транзакции",
            required = true,
            content =
                @Content(
                    schema = @Schema(implementation = UpdateTransactionRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "amount": "-98.23",
                      "currency": "USD",
                      "description": "Какое-то описание",
                      "bankAccountUuid": "abfcea43-8922-4d48-b1e6-dfc6e97f75bb",
                      "categoryUuid": "abfcea43-8922-4d48-b1e6-dfc6e97f75bb",
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Транзакция успешно обновлена",
          content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
    })
public @interface UpdateTransactionOperation {}
