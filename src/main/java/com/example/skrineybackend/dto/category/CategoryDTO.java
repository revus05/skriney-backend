package com.example.skrineybackend.dto.category;

import com.example.skrineybackend.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class CategoryDTO {
    @Schema(description = "Category uuid")
    private String uuid;

    @Schema(description = "Category title")
    private String title;

    @Schema(description = "Category emoji")
    private String emoji;

    @Schema(description = "Creation timestamp", type = "string", format = "date-time")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", type = "string", format = "date-time")
    private Instant updatedAt;

    public CategoryDTO(Category category) {
        this.uuid = category.getUuid();
        this.title = category.getTitle();
        this.emoji = category.getEmoji();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}
