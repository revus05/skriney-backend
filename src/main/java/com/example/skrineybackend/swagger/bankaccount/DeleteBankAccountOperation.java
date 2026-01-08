package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
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
    summary = "Удаление счета",
    description = "Удаляет счет пользователя",
    requestBody = @RequestBody(description = "Данные для удаления счета"),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Счет успешно удален",
          content = @Content(schema = @Schema(implementation = BankAccountDTO.class))),
    })
public @interface DeleteBankAccountOperation {}
