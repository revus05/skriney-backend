package com.example.skrineybackend.dto;

import com.example.skrineybackend.entity.Account;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class AccountDTO {
    @Schema(description = "Account UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "Money on the account", requiredMode = Schema.RequiredMode.REQUIRED)
    private double balance;

    @Schema(description = "Account currency", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency currency;

    @Schema(description = "Account title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Account's card color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String color;

    @Schema(description = "Are account money used to calculate total user balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean isInTotalBalance;

    @Schema(description = "Percent of balance change", requiredMode = Schema.RequiredMode.REQUIRED)
    private double changePercent;

    @Schema(description = "Optional user's description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Optional user's image", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String image;

    @Schema(description = "Creation timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Schema(description = "Last update timestamp", type = "string", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant updatedAt;

    public AccountDTO(Account account) {
        this.uuid = account.getUuid();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.title = account.getTitle();
        this.color = account.getColor();
        this.isInTotalBalance = account.isInTotalBalance();
        this.changePercent = account.getChangePercent();
        this.description = account.getDescription();
        this.image = account.getImage();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
