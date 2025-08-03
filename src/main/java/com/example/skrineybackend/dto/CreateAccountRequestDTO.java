package com.example.skrineybackend.dto;

import com.example.skrineybackend.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAccountRequestDTO {
    @Schema(description = "Initial account balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double balance;

    @Schema(description = "Account currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Currency currency;

    @Schema(description = "Account name", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Название счета обязательно")
    @Size(max = 64, message = "Название счета не может быть длиннее 64 символов")
    private String name;

    @Schema(description = "Account's card colour", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String colour;

    @Schema(description = "Are account money used to calculate total user balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean isInTotalBalance;

    @Schema(description = "Optional user's description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(description = "Optional user's image", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String image;
}
