package com.example.skrineybackend.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCategoryRequestDTO {
    @Schema(description = "Bank Account title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String title;

    @Schema(description = "Bank Account emoji", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String emoji;
}
