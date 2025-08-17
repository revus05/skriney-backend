package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.DeleteBankAccountRequestDTO;
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
    summary = "Удаление счета",
    description = "Удаляет счет пользователя",
    requestBody = @RequestBody(
        description = "Данные для удаления счета",
        content = @Content(
            schema = @Schema(implementation = DeleteBankAccountRequestDTO.class),
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
            description = "Счет успешно удален",
            content = @Content(schema = @Schema(implementation = BankAccountDTO.class))
        ),
    }
)
public @interface DeleteBankAccountOperation {
}
