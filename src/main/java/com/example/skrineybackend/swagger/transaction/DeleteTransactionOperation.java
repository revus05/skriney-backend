package com.example.skrineybackend.swagger.transaction;

import com.example.skrineybackend.dto.transaction.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
    requestBody = @RequestBody(description = "Данные для удаления транзакции"),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Транзакция успешно удалена",
          content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
    })
public @interface DeleteTransactionOperation {}
