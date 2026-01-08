package com.example.skrineybackend.swagger.category;

import com.example.skrineybackend.dto.category.CategoryStatDTO;
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
    summary = "Получение статистики категорий",
    description = "Получение статистики всех категорий пользователя",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Статистика категорий получена успешно",
          content =
              @Content(
                  array = @ArraySchema(schema = @Schema(implementation = CategoryStatDTO.class)))),
    })
public @interface GetCategoryStatsOperation {}
