package com.example.skrineybackend.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record BalanceSummaryDTO(
    @Schema(description = "User balance", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal totalBalanceInUsd,
    @Schema(description = "Sum of income", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal totalIncomeInUsd,
    @Schema(description = "Sum of expenses", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal totalExpenseInUsd) {
  public BalanceSummaryDTO(
      BigDecimal totalBalanceInUsd, BigDecimal totalIncomeInUsd, BigDecimal totalExpenseInUsd) {
    this.totalBalanceInUsd = totalBalanceInUsd;
    this.totalIncomeInUsd = totalIncomeInUsd;
    this.totalExpenseInUsd = totalExpenseInUsd.abs();
  }
}
