package com.example.skrineybackend.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record CategoryStatDTO(
    @Schema(description = "Category uuid", requiredMode = Schema.RequiredMode.REQUIRED) String uuid,
    @Schema(description = "Category total spent", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal totalExpenses) {}
