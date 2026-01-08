package com.example.skrineybackend.swagger.file;

import com.example.skrineybackend.dto.file.UploadFileRequestDTO;
import com.example.skrineybackend.dto.file.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Загрузка файла",
    description = "Загружает файл на сервер и возвращает путь к нему",
    requestBody =
        @RequestBody(
            description = "Данные для обновления изображения профиля",
            content =
                @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(implementation = UploadFileRequestDTO.class))),
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Файл успешно загружен",
          content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))),
    })
public @interface UploadFileOperation {}
