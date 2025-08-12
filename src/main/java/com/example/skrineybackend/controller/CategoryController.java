package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.CategoryDTO;
import com.example.skrineybackend.dto.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.Response;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Категории", description = "Управление категориями транзакций пользователей")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "Создание категории",
        description = "Создает категорию транзакции для пользователя",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания категории",
            content = @Content(
                schema = @Schema(implementation = CreateCategoryRequestDTO.class),
                examples = @ExampleObject(
                    name = "Пример запроса",
                    value = "{\"title\":\"category name\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Категория успешно создана",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
        }
    )
    @PostMapping("/create")
    public Response createCategory(@Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO createdCategory = categoryService.createCategory(createCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно создана", HttpStatus.CREATED, createdCategory);
    }

    @Operation(
        summary = "Получение категорий",
        description = "Получение всех категорий пользователя",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Категории получены успешно",
                content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))
                )
            ),
        }
    )
    @GetMapping()
    public Response getCategories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CategoryDTO> categories = categoryService.getCategories(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категории получены успешно", HttpStatus.OK, categories);
    }
}
