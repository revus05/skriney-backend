package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.UpdateDefaultCategoryRequestDTO;
import com.example.skrineybackend.dto.UpdateDefaultCurrencyRequestDTO;
import com.example.skrineybackend.dto.UserSettingsDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.exception.NoUserSettingsFoundException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {
    private final UserSettingsRepo userSettingsRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    public UserSettingsService(UserSettingsRepo userSettingsRepo, UserRepo userRepo, CategoryRepo categoryRepo) {
        this.userSettingsRepo = userSettingsRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    public UserSettingsDTO getUserSettings(String userUuid) throws NoUserFoundException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateDefaultCurrency(UpdateDefaultCurrencyRequestDTO updateDefaultCurrencyRequestDTO, String userUuid) throws NoUserFoundException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        userSettings.setDefaultCurrency(updateDefaultCurrencyRequestDTO.getCurrency());
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }

    public UserSettingsDTO updateDefaultCategory(UpdateDefaultCategoryRequestDTO updateDefaultCategoryRequestDTO, String userUuid) throws NoUserFoundException, NoUserSettingsFoundException, NoCategoryFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        Category category = categoryRepo.findById(updateDefaultCategoryRequestDTO.getUuid()).orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        userSettings.setDefaultCategory(category);
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }
}
