package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.UserSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserSettingsRepo extends CrudRepository<UserSettings, String> {
    Optional<UserSettings> findByUserUuid(String userUuid);
    List<UserSettings> findAllByDefaultBankAccountUuid(String defaultBankAccountUuid);
    Optional<UserSettings> findByDefaultCategoryUuid(String defaultCategoryUuid);
}
