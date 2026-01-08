package com.example.skrineybackend.swagger.transaction;

import com.example.skrineybackend.dto.transaction.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Получение транзакций со всех счетов",
    description = "Получение всех транзакций пользователя",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Транзакции получены успешно",
          content =
              @Content(
                  array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class)))),
    })
public @interface GetTransactionsOperation {}
