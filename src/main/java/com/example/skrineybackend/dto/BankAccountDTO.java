package com.example.skrineybackend.dto;

import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class BankAccountDTO {
    @Schema(description = "Bank Account UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "Money on the bank account", requiredMode = Schema.RequiredMode.REQUIRED)
    private double balance;

    @Schema(description = "Bank Account currency", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency currency;

    @Schema(description = "Bank Account title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Bank Account's card color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String color;

    @Schema(description = "Are bank account money used to calculate total user balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

    public BankAccountDTO(BankAccount bankAccount) {
        this.uuid = bankAccount.getUuid();
        this.balance = bankAccount.getBalance();
        this.currency = bankAccount.getCurrency();
        this.title = bankAccount.getTitle();
        this.color = bankAccount.getColor();
        this.isInTotalBalance = bankAccount.isInTotalBalance();
        this.changePercent = bankAccount.getChangePercent();
        this.description = bankAccount.getDescription();
        this.image = bankAccount.getImage();
        this.createdAt = bankAccount.getCreatedAt();
        this.updatedAt = bankAccount.getUpdatedAt();
    }
}
