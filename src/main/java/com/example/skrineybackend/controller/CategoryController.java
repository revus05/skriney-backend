package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.category.*;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.service.CategoryService;
import com.example.skrineybackend.swagger.category.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Категории", description = "Управление категориями транзакций пользователей")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetCategoriesOperation
    @GetMapping()
    public Response getCategories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CategoryDTO> categories = categoryService.getCategories(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категории получены успешно", HttpStatus.OK, categories);
    }

    @CreateCategoryOperation
    @PostMapping("/create")
    public Response createCategory(@Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO createdCategory = categoryService.createCategory(createCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно создана", HttpStatus.CREATED, createdCategory);
    }

    @DeleteCategoryOperation
    @DeleteMapping("/delete")
    public Response deleteCategory(@Valid @RequestBody DeleteCategoryRequestDTO deleteCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO deletedCategory = categoryService.deleteCategory(deleteCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно удалена", HttpStatus.OK, deletedCategory);
    }

    @GetCategoryStatsOperation
    @GetMapping("/stats")
    public Response getCategoryStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CategoryStatDTO> categoryStats = categoryService.getCategoryStats(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Статистика категорий получена успешно", HttpStatus.OK, categoryStats);
    }

    @UpdateCategoryOperation
    @PatchMapping("/{categoryUuid}")
    public Response updateCategory(@PathVariable String categoryUuid, @Valid @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryUuid, updateCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория обновлена успешно", HttpStatus.OK, updatedCategory);
    }
}
