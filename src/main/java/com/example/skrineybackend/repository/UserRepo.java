package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  Optional<User> findByTelegramId(Long telegramId);
}
