package com.example.skrineybackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UploadFileResponseDTO {
  @Schema(description = "Uploaded file path", requiredMode = Schema.RequiredMode.REQUIRED)
  private final String filepath;
}
