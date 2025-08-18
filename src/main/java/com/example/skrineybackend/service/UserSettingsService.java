package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.usersettings.UpdateDefaultCategoryRequestDTO;
import com.example.skrineybackend.dto.usersettings.UpdateDefaultCurrencyRequestDTO;
import com.example.skrineybackend.dto.usersettings.UpdateThemeRequestDTO;
import com.example.skrineybackend.dto.usersettings.UserSettingsDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.exception.NoUserSettingsFoundException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepo userSettingsRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

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

    public UserSettingsDTO updateTheme(UpdateThemeRequestDTO updateThemeRequestDTO, String userUuid) throws NoUserFoundException, NoUserSettingsFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));
        UserSettings userSettings = userSettingsRepo.findByUserUuid(userUuid).orElseThrow(() -> new NoUserSettingsFoundException("Нет настроек для пользователя"));

        userSettings.setUserTheme(updateThemeRequestDTO.getTheme());
        userSettingsRepo.save(userSettings);

        return new UserSettingsDTO(userSettings);
    }
}
