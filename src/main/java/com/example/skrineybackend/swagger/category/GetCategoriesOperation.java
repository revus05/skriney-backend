package com.example.skrineybackend.swagger.category;

import com.example.skrineybackend.dto.category.CategoryDTO;
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
    summary = "Получение категорий",
    description = "Получение всех категорий пользователя",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Категории получены успешно",
          content =
              @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))),
    })
public @interface GetCategoriesOperation {}
