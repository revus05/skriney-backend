package com.example.skrineybackend.swagger.balance;

import com.example.skrineybackend.dto.balance.DailyBalanceDTO;
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
    summary = "Получение ежедневных срезов баланса",
    description = "Получение всех ежедневных срезов баланса со всех счетов пользователя",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Ежедневные балансы получены успешно",
          content =
              @Content(
                  array = @ArraySchema(schema = @Schema(implementation = DailyBalanceDTO.class)))),
    })
public @interface GetUserBalanceTimelineOperation {}
