package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
    User findByEmail(String email);
    User findByUsername(String username);
}
