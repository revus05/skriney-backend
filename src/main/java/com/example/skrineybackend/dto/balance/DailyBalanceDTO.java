package com.example.skrineybackend.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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

    public DailyBalanceDTO(
            LocalDate date,
            BigDecimal dailyIncome,
            BigDecimal dailyExpenses,
            BigDecimal dailyChange,
            BigDecimal totalBalance,
            String bankAccountUuid)
    {
        this.uuid = UUID.randomUUID().toString();
        this.date = date;
        this.dailyIncome = dailyIncome;
        this.dailyExpenses = dailyExpenses;
        this.dailyChange = dailyChange;
        this.totalBalance = totalBalance;
        this.bankAccountUuid = bankAccountUuid;
    }
}
