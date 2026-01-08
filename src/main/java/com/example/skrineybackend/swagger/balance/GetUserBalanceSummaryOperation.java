package com.example.skrineybackend.swagger.balance;

import com.example.skrineybackend.dto.balance.BalanceSummaryDTO;
import com.example.skrineybackend.enums.BalancePeriod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    parameters = {
      @Parameter(
          name = "period",
          description = "Срок за который нужно получить информацию о балансе",
          in = ParameterIn.PATH,
          schema = @Schema(implementation = BalancePeriod.class)),
      @Parameter(
          name = "bankAccountUuid",
          description = "Uuid банковского счета",
          in = ParameterIn.PATH,
          schema = @Schema(type = "string", format = "uuid"))
    },
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Общий баланс получен успешно",
          content = @Content(schema = @Schema(implementation = BalanceSummaryDTO.class))),
    })
public @interface GetUserBalanceSummaryOperation {}
