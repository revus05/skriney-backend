package com.example.skrineybackend.dto.category;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryStatDTO {
  @Schema(description = "Category uuid", requiredMode = Schema.RequiredMode.REQUIRED)
  String uuid;

  @Schema(description = "Category total balance", requiredMode = Schema.RequiredMode.REQUIRED)
  BigDecimal totalBalanceInUsd;

  @Schema(description = "Each currency balance", requiredMode = Schema.RequiredMode.REQUIRED)
  private Map<Currency, BigDecimal> currencyBalances;

  public CategoryStatDTO(
      String categoryUuid,
      BigDecimal totalBalanceInUsd,
      Map<Currency, BigDecimal> currencyBalances) {
    this.uuid = categoryUuid;
    this.totalBalanceInUsd = totalBalanceInUsd;
    this.currencyBalances = currencyBalances;
  }
}
