package com.example.skrineybackend.swagger.category;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
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
    summary = "Обновление категории у пользователя",
    description = "Обновляет данные существующей категории пользователя",
    parameters = {
      @Parameter(
          name = "categoryUuid",
          description = "UUID категории, которую нужно обновить",
          required = true,
          in = ParameterIn.PATH,
          schema = @Schema(type = "string", format = "uuid"))
    },
    requestBody =
        @RequestBody(
            description = "Данные для обновления категории",
            required = true,
            content =
                @Content(
                    schema = @Schema(implementation = UpdateCategoryRequestDTO.class),
                    examples =
                        @ExampleObject(
                            name = "Пример запроса",
                            value =
                                """
                    {
                      "title": "Новая категория",
                      "emoji": "😎"
                    }"""))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Счет успешно обновлен",
          content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
    })
public @interface UpdateCategoryOperation {}
