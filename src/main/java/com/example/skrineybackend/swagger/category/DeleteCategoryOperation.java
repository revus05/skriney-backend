package com.example.skrineybackend.swagger.category;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.DeleteCategoryRequestDTO;
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
    summary = "Удаление категории",
    description = "Удаляет категорию транзакции у пользователя",
    requestBody = @RequestBody(
        description = "Данные для удаления категории",
        content = @Content(
            schema = @Schema(implementation = DeleteCategoryRequestDTO.class),
            examples = @ExampleObject(
                name = "Пример запроса",
                value = """
                    {
                        "uuid": "bcbfe240-a61b-4e21-94f0-188bddf4a04e"
                    }"""
            )
        )
    ),
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Категория успешно удалена",
            content = @Content(schema = @Schema(implementation = CategoryDTO.class))
        ),
    }
)
public @interface DeleteCategoryOperation {
}
