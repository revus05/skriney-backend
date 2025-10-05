package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Получение конкретного счета пользователя",
    description = "Получение конкретного счета пользователя",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Счет пользователя успешно получен",
            content = @Content(schema = @Schema(implementation = BankAccountDTO.class))
        ),
    }
)
public @interface GetOneBankAccountOperation {
}
