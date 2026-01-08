package com.example.skrineybackend.swagger.category;

import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
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
    summary = "Создание категории",
    description = "Создает категорию транзакции для пользователя",
    requestBody =
        @RequestBody(
            description = "Данные для создания категории",
            content =
                @Content(
                    schema = @Schema(implementation = CreateCategoryRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                       "title": "Продукты"
                     }"""))),
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Категория успешно создана",
          content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
    })
public @interface CreateCategoryOperation {}
