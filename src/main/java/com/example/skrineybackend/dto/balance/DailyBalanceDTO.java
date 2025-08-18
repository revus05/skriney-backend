package com.example.skrineybackend.dto.balance;

import com.example.skrineybackend.entity.DailyBalance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DailyBalanceDTO {
    @Schema(description = "Daily balance uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

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

    @Schema(description = "Bank account uuid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bankAccountUuid;

    public DailyBalanceDTO(DailyBalance dailyBalance) {
        this.uuid = dailyBalance.getUuid();
        this.date = dailyBalance.getDate();
        this.totalBalance = dailyBalance.getTotalBalance();
        this.dailyChange = dailyBalance.getDailyChange();
        this.dailyIncome = dailyBalance.getDailyIncome();
        this.dailyExpenses = dailyBalance.getDailyExpenses().abs();
        this.bankAccountUuid = dailyBalance.getBankAccount().getUuid();
    }
}
