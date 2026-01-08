package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
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
    summary = "Получение счетов пользователя",
    description = "Получение всех счетов пользователя",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Счета пользователя успешно получены",
          content =
              @Content(
                  array = @ArraySchema(schema = @Schema(implementation = BankAccountDTO.class)))),
    })
public @interface GetAllBankAccountsOperation {}
