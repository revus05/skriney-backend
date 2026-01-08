package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.file.UploadFileResponseDTO;
import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.swagger.file.UploadFileOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@Tag(name = "Файлы", description = "Управление загрузкой файлов")
public class FileController {

  @UploadFileOperation
  @PostMapping("/upload")
  public Response uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      Path uploadPath = Paths.get("uploads");
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Path filePath = uploadPath.resolve(fileName);

      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      UploadFileResponseDTO response = new UploadFileResponseDTO("/files/" + fileName);

      return new Response("Файл успешно загружен", HttpStatus.OK, response);
    } catch (IOException e) {
      return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
