package com.example.skrineybackend.dto.bankaccount;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateBankAccountRequestDTO {
    @Schema(description = "Initial bank account balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal balance;

    @Schema(description = "Bank account currency", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Валюта обязательна")
    private Currency currency;

    @Schema(description = "Bank account title", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Название счета обязательно")
    @Size(max = 64, message = "Название счета не может быть длиннее 64 символов")
    private String title;

    @Schema(description = "Bank account's card color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String color;

    @Schema(description = "Are bank account money used to calculate total user balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean isInTotalBalance;

    @Schema(description = "Optional user's description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Optional user's image", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String image;
}
