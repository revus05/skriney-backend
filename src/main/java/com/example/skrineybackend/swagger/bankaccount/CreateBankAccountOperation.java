package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
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
    summary = "Создание счета",
    description = "Создает счет для пользователя",
    requestBody =
        @RequestBody(
            description = "Данные для создания счета",
            content =
                @Content(
                    schema = @Schema(implementation = CreateBankAccountRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                       "title": "Зарплатный",
                       "currency": "BYN",
                       "balance": 4900
                     }"""))),
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Счет успешно создан",
          content = @Content(schema = @Schema(implementation = BankAccountDTO.class))),
    })
public @interface CreateBankAccountOperation {}
