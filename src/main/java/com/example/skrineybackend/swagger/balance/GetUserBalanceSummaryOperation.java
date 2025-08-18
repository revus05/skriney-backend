package com.example.skrineybackend.swagger.balance;

import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Получение общего баланса пользователя",
    description = "Получение общего баланса пользователя со всех счетов пользователя",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Общий баланс получен успешно",
            content = @Content(
                schema = @Schema(implementation = BalanceSummaryDTO.class)
            )
        ),
    }
)
public @interface GetUserBalanceSummaryOperation {
}
