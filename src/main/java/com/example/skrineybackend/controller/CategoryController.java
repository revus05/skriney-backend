package com.example.skrineybackend.controller;

import com.example.skrineybackend.applicationservice.CategoryApplicationService;
import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.swagger.category.CreateCategoryOperation;
import com.example.skrineybackend.swagger.category.DeleteCategoryOperation;
import com.example.skrineybackend.swagger.category.GetCategoriesOperation;
import com.example.skrineybackend.swagger.category.GetCategoryStatsOperation;
import com.example.skrineybackend.swagger.category.UpdateCategoryOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Tag(name = "Категории", description = "Управление категориями транзакций пользователей")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryApplicationService categoryApplicationService;

  @GetCategoriesOperation
  @GetMapping()
  public Response getCategories() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<CategoryDTO> categories =
        categoryApplicationService.getCategories(((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Категории получены успешно", HttpStatus.OK, categories);
  }

  @CreateCategoryOperation
  @PostMapping()
  public Response createCategory(
      @Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CategoryDTO createdCategory =
        categoryApplicationService.createCategory(
            createCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Категория успешно создана", HttpStatus.CREATED, createdCategory);
  }

  @DeleteCategoryOperation
  @DeleteMapping("{uuid}")
  public Response deleteCategory(@PathVariable String uuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CategoryDTO deletedCategory =
        categoryApplicationService.deleteCategory(
            uuid, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Категория успешно удалена", HttpStatus.OK, deletedCategory);
  }

  @UpdateCategoryOperation
  @PatchMapping("{uuid}")
  public Response updateCategory(
      @PathVariable String uuid,
      @Valid @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CategoryDTO updatedCategory =
        categoryApplicationService.updateCategory(
            uuid, updateCategoryRequestDTO, ((UserDetails) auth.getPrincipal()).getUsername());
    return new Response("Категория обновлена успешно", HttpStatus.OK, updatedCategory);
  }

  @GetCategoryStatsOperation
  @GetMapping("/stats")
  public Response getCategoryStats(
      @RequestParam(required = false, defaultValue = "LAST_30_DAYS") BalancePeriod period,
      @RequestParam(required = false) String bankAccountUuid) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<CategoryStatDTO> categoryStats =
        categoryApplicationService.getCategoryStats(
            ((UserDetails) auth.getPrincipal()).getUsername(), period, bankAccountUuid);
    return new Response("Статистика категорий получена успешно", HttpStatus.OK, categoryStats);
  }
}
