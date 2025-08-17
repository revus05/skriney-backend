package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.DeleteCategoryRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.service.CategoryService;
import com.example.skrineybackend.swagger.category.CreateCategoryOperation;
import com.example.skrineybackend.swagger.category.DeleteCategoryOperation;
import com.example.skrineybackend.swagger.category.GetCategoriesOperation;
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

    @CreateCategoryOperation
    @PostMapping("/create")
    public Response createCategory(@Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO createdCategory = categoryService.createCategory(createCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно создана", HttpStatus.CREATED, createdCategory);
    }

    @GetCategoriesOperation
    @GetMapping()
    public Response getCategories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CategoryDTO> categories = categoryService.getCategories(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категории получены успешно", HttpStatus.OK, categories);
    }

    @DeleteCategoryOperation
    @DeleteMapping("/delete")
    public Response deleteCategory(@Valid @RequestBody DeleteCategoryRequestDTO deleteCategoryRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CategoryDTO deletedCategory = categoryService.deleteCategory(deleteCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Категория успешно удалена", HttpStatus.OK, deletedCategory);
    }
}
