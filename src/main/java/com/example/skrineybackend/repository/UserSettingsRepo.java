package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.UserSettings;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserSettingsRepo extends CrudRepository<UserSettings, String> {
  Optional<UserSettings> findByUserUuid(String userUuid);

  Optional<UserSettings> findByDefaultBankAccountUuid(String defaultBankAccountUuid);

  Optional<UserSettings> findByDefaultCategoryUuid(String defaultCategoryUuid);
}
