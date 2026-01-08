package com.example.skrineybackend.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadFileRequestDTO {
  @Schema(
      description = "Файл для загрузки",
      type = "string",
      format = "binary",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private MultipartFile file;
}
