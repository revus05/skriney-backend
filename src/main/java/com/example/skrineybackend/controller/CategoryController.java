package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.enums.BalancePeriod;
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
    @PostMapping()
    public Response createCategory(@Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO createdCategory = categoryService.createCategory(createCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно создана", HttpStatus.CREATED, createdCategory);
    }

    @DeleteCategoryOperation
    @DeleteMapping("{uuid}")
    public Response deleteCategory(@PathVariable String uuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO deletedCategory = categoryService.deleteCategory(uuid, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно удалена", HttpStatus.OK, deletedCategory);
    }

    @GetCategoryStatsOperation
    @GetMapping("/stats")
    public Response getCategoryStats(@RequestParam(required = false, defaultValue = "LAST_30_DAYS") BalancePeriod period, @RequestParam(required = false) String bankAccountUuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CategoryStatDTO> categoryStats = categoryService.getCategoryStats(((UserDetails) auth.getPrincipal()).getUsername(), period, bankAccountUuid);
        return new Response("Статистика категорий получена успешно", HttpStatus.OK, categoryStats);
    }

    @UpdateCategoryOperation
    @PatchMapping("{uuid}")
    public Response updateCategory(@PathVariable String uuid, @Valid @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO updatedCategory = categoryService.updateCategory(uuid, updateCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория обновлена успешно", HttpStatus.OK, updatedCategory);
    }
}
