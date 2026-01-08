package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CategoryStatDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.UpdateCategoryRequestDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.enums.BalancePeriod;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final UserSettingsRepo userSettingsRepo;

    public List<CategoryStatDTO> getCategoryStats(String userUuid, BalancePeriod period, String bankAccountUuid) throws UnauthorizedException {
        Instant startDate = calculateFromDateTime(period);
        if (startDate == null) {
            startDate = Instant.EPOCH;
        }
        return transactionRepo.getUserCategoryStats(userUuid, startDate, bankAccountUuid);
    }

    private Instant calculateFromDateTime(BalancePeriod period) {
        Instant now = Instant.now();

        return switch (period) {
            case LAST_7_DAYS   -> now.minus(7, ChronoUnit.DAYS);
            case LAST_30_DAYS  -> now.minus(30, ChronoUnit.DAYS);
            case LAST_3_MONTHS -> now.minus(90, ChronoUnit.DAYS);
            case LAST_1_YEAR   -> now.minus(365, ChronoUnit.DAYS);
            case ALL_TIME      -> Instant.EPOCH;
        };
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

        Optional<UserSettings> optionalSettings = userSettingsRepo.findByDefaultCategoryUuid(category.getUuid());

        optionalSettings.ifPresent(userSettings -> updateDefaultCategoryAfterDeletion(userSettings, uuid));

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

    private void updateDefaultCategoryAfterDeletion(UserSettings userSettings, String uuid) {
        List<Category> remainingCategories = categoryRepo.findAllByUser_UuidOrderByCreatedAt(userSettings.getUser().getUuid())
                .stream()
                .filter(c -> !c.getUuid().equals(uuid))
                .toList();

        Category newDefault = remainingCategories.isEmpty() ? null : remainingCategories.get(0);
        userSettings.setDefaultCategory(newDefault);

        userSettingsRepo.save(userSettings);
    }
}
