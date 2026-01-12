package com.example.skrineybackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadFileResponseDTO(
    @Schema(description = "Uploaded file path", requiredMode = Schema.RequiredMode.REQUIRED)
        String filepath) {}
