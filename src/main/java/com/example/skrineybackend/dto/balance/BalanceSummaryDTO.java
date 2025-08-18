package com.example.skrineybackend.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BalanceSummaryDTO(
        @Schema(description = "Total sum for this day", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal totalBalance,
        @Schema(description = "Sum of income for last 30 days", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal totalIncome,
        @Schema(description = "Sum of expenses for last 30 days", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal totalExpense
    ) {
    public BalanceSummaryDTO(BigDecimal totalBalance, BigDecimal totalIncome, BigDecimal totalExpense) {
        this.totalBalance = totalBalance;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense.abs();
    }
}
