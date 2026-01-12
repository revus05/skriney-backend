package com.example.skrineybackend.applicationservice;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.entity.*;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.service.BalanceService;
import com.example.skrineybackend.service.CategoryService;
import com.example.skrineybackend.service.TransactionService;
import com.example.skrineybackend.service.UserSettingsService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryApplicationService {
  private final CategoryService categoryService;
  private final UserSettingsService userSettingsService;
  private final BalanceService balanceService;

  private final UserRepo userRepo;
  private final CategoryRepo categoryRepo;
  private final TransactionService transactionService;

  public List<CategoryDTO> getCategories(String userUuid) throws UnauthorizedException {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return categoryService.getCategories(userUuid).stream().map(CategoryDTO::new).toList();
  }

  @Transactional
  public CategoryDTO createCategory(CreateCategoryRequestDTO dto, String userUuid)
      throws UnauthorizedException {
    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    Category category = categoryService.createCategory(dto, user);

    if (user.getCategories().isEmpty()) {
      user.getSettings().setDefaultCategory(category);
    }

    return new CategoryDTO(category);
  }

  @Transactional
  public CategoryDTO deleteCategory(String uuid, String userUuid) {
    Category category = getCategoryIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    userSettingsService.updateDefaultCategoryAfterDeletion(uuid);

    categoryService.deleteCategory(category);

    return new CategoryDTO(category);
  }

  @Transactional
  public CategoryDTO updateCategory(String uuid, UpdateCategoryRequestDTO dto, String userUuid) {
    Category category = getCategoryIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    Category updatedCategory = categoryService.updateCategory(category, dto);

    return new CategoryDTO(updatedCategory);
  }

  public List<CategoryStatDTO> getCategoryStats(
      String userUuid, BalancePeriod period, String bankAccountUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    Instant startDate = balanceService.getStartDateTimeFromPeriod(period);
    if (startDate == null) {
      startDate = Instant.EPOCH;
    }

    List<Transaction> transactions =
        transactionService.getTransactions(userUuid, bankAccountUuid, startDate);

    return categoryService.getCategoryStats(transactions);
  }

  private Category getCategoryIfUserAuthorizedAndOwnsIt(String categoryUuid, String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return categoryRepo
        .findByUuidAndUser_Uuid(categoryUuid, userUuid)
        .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена"));
  }
}
