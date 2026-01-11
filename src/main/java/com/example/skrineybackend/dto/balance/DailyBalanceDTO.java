package com.example.skrineybackend.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyBalanceDTO {
  @Schema(description = "Day date", requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate date;

  @Schema(description = "Total balance for this day", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal totalBalance;

  @Schema(description = "Total daily balance change", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal dailyChange;

  @Schema(description = "Total daily income", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal dailyIncome;

  @Schema(description = "Total daily expenses", requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal dailyExpenses;

  public DailyBalanceDTO(
      LocalDate date,
      BigDecimal dailyIncome,
      BigDecimal dailyExpenses,
      BigDecimal dailyChange,
      BigDecimal totalBalance) {
    this.date = date;
    this.dailyIncome = dailyIncome;
    this.dailyExpenses = dailyExpenses;
    this.dailyChange = dailyChange;
    this.totalBalance = totalBalance;
  }
}
