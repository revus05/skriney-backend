package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final UserSettingsRepo userSettingsRepo;

    public List<CategoryStatDTO> getCategoryStats(String userUuid) throws UnauthorizedException {
        return transactionRepo.getUserCategoryStats(userUuid);
    }

    public CategoryDTO createCategory(CreateCategoryRequestDTO createCategoryRequestDTO, String userUuid) throws UnauthorizedException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        Category category = new Category(createCategoryRequestDTO);

        if (user.getCategories().isEmpty()) {
            user.getSettings().setDefaultCategory(category);
        }

        category.setUser(user);

        return new CategoryDTO(categoryRepo.save(category));
    }

    public List<CategoryDTO> getCategories(String userUuid) throws UnauthorizedException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        return categoryRepo.findAllByUser_UuidOrderByCreatedAt(userUuid).stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO deleteCategory(String uuid, String userUuid) throws UnauthorizedException, NoCategoryFoundException {
        userRepo.findById(userUuid)
                .orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        Category category = categoryRepo.findByUuidAndUser_Uuid(uuid, userUuid)
                .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        List<UserSettings> settingsList = userSettingsRepo.findAllByDefaultCategoryUuid(category.getUuid());

        for (UserSettings settings : settingsList) {
            settings.setDefaultCategory(null);
        }

        categoryRepo.delete(category);

        return new CategoryDTO(category);
    }

    public CategoryDTO updateCategory(String uuid, UpdateCategoryRequestDTO updateCategoryRequestDTO, String userUuid) throws UnauthorizedException, NoCategoryFoundException {
        userRepo.findById(userUuid)
                .orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        Category category = categoryRepo.findByUuidAndUser_Uuid(uuid, userUuid)
                .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        if (updateCategoryRequestDTO.getTitle() != null) {
            category.setTitle(updateCategoryRequestDTO.getTitle());
        }
        if (updateCategoryRequestDTO.getEmoji() != null) {
            category.setEmoji(updateCategoryRequestDTO.getEmoji());
        }

        categoryRepo.save(category);

        return new CategoryDTO(category);
    }
}
