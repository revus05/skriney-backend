package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.CurrencyRate;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.CurrencyRateRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepo categoryRepo;
  private final CurrencyRateRepo currencyRateRepo;

  public List<CategoryStatDTO> getCategoryStats(List<Transaction> transactions) {
    Map<Category, BigDecimal> categoryAmount = new HashMap<>();
    Map<Category, Map<Currency, BigDecimal>> currencyBalances = new HashMap<>();
    for (Transaction transaction : transactions) {
      if (transaction.getCategory() == null) {
        continue;
      }

      BigDecimal rate =
          currencyRateRepo
              .findTopByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
                  "USD", transaction.getCurrency().name())
              .orElse(new CurrencyRate())
              .getRate();

      int CURRENCY_SCALE =
          java.util.Currency.getInstance(transaction.getCurrency().name())
              .getDefaultFractionDigits();

      BigDecimal amountInUsd =
          transaction.getAmount().divide(rate, CURRENCY_SCALE, RoundingMode.HALF_EVEN);

      categoryAmount.merge(transaction.getCategory(), amountInUsd, BigDecimal::add);

      currencyBalances
          .computeIfAbsent(transaction.getCategory(), k -> new HashMap<>())
          .merge(transaction.getCurrency(), transaction.getAmount(), BigDecimal::add);
    }

    List<CategoryStatDTO> result = new ArrayList<>();
    for (Map.Entry<Category, BigDecimal> entry : categoryAmount.entrySet()) {
      result.add(
          new CategoryStatDTO(
              entry.getKey().getUuid(), entry.getValue(), currencyBalances.get(entry.getKey())));
    }

    return result;
  }

  public Category createCategory(CreateCategoryRequestDTO dto, User user) {
    return categoryRepo.save(new Category(dto, user));
  }

  public List<Category> getCategories(String userUuid) {
    return categoryRepo.findAllByUser_UuidOrderByCreatedAt(userUuid);
  }

  public void deleteCategory(Category category) {
    categoryRepo.delete(category);
  }

  public Category updateCategory(
      Category category, UpdateCategoryRequestDTO updateCategoryRequestDTO) {
    if (updateCategoryRequestDTO.getTitle() != null) {
      category.setTitle(updateCategoryRequestDTO.getTitle());
    }
    if (updateCategoryRequestDTO.getEmoji() != null) {
      category.setEmoji(updateCategoryRequestDTO.getEmoji());
    }

    categoryRepo.save(category);

    return category;
  }
}
