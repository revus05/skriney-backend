package com.example.skrineybackend.swagger.bankaccount;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
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
    summary = "Обновление счета пользователя",
    description = "Обновляет данные существующего банковского счета пользователя",
    parameters = {
      @Parameter(
          name = "bankAccountUuid",
          description = "UUID банковского счета, который нужно обновить",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(type = "string", format = "uuid"))
    },
    requestBody =
        @RequestBody(
            description = "Данные для обновления счета",
            required = true,
            content =
                @Content(
                    schema = @Schema(implementation = UpdateBankAccountRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "title": "Новый счет",
                      "currency": "USD",
                      "emoji": "😎"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Счет успешно обновлен",
          content = @Content(schema = @Schema(implementation = BankAccountDTO.class))),
    })
public @interface UpdateBankAccountOperation {}
