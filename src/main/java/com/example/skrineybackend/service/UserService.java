package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.RegisterRequestDTO;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.InvalidCredentialsException;
import com.example.skrineybackend.exception.UserAlreadyExistsException;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(RegisterRequestDTO requestBody) throws UserAlreadyExistsException {
        checkUserExists(requestBody.getEmail(), requestBody.getUsername());

        String encodedPassword = passwordEncoder.encode(requestBody.getPassword());
        requestBody.setPassword(encodedPassword);

        return new UserDTO(userRepo.save(new User(requestBody)));
    }

    public UserDTO loginUser(LoginRequestDTO loginRequestDTO) throws InvalidCredentialsException {
        User foundUser = userRepo.findByEmail(loginRequestDTO.getEmail())
            .filter(user -> passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword()))
            .orElseThrow(() -> new InvalidCredentialsException("Нет пользователя с введенными данными"));

        return new UserDTO(foundUser);
    }

    private void checkUserExists(String email, String username) {
        userRepo.findByEmail(email)
            .ifPresent(user -> {
                throw new UserAlreadyExistsException("email", "Пользователь с таким email уже существует");
            });

        userRepo.findByUsername(username)
            .ifPresent(user -> {
                throw new UserAlreadyExistsException("username", "Имя пользователя занято");
            });
    }
}
