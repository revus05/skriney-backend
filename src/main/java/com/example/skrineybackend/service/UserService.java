package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.RegisterRequestDTO;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exeption.InvalidCredentialsException;
import com.example.skrineybackend.exeption.InvalidEmailException;
import com.example.skrineybackend.exeption.InvalidInputException;
import com.example.skrineybackend.exeption.UserAlreadyExistsException;
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

    public User registerUser(RegisterRequestDTO requestBody) throws UserAlreadyExistsException, InvalidEmailException, InvalidInputException {
        User foundUserWithEmail = userRepo.findByEmail(requestBody.getEmail());
        if (foundUserWithEmail != null) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        User foundUserWithUsername = userRepo.findByUsername(requestBody.getUsername());
        if (foundUserWithUsername != null) {
            throw new UserAlreadyExistsException("Имя пользователя занято");
        }

        User newUser = new User();
        newUser.setUsername(requestBody.getUsername());
        newUser.setEmail(requestBody.getEmail());

        String encodedPassword = passwordEncoder.encode(requestBody.getPassword());
        newUser.setPassword(encodedPassword);

        return userRepo.save(newUser);
    }

    public UserDTO loginUser(LoginRequestDTO loginRequestDTO) throws InvalidEmailException, InvalidCredentialsException {
        User foundUser = userRepo.findByEmail(loginRequestDTO.getEmail());
        if (foundUser == null || !passwordEncoder.matches(loginRequestDTO.getPassword(), foundUser.getPassword())) {
            throw new InvalidCredentialsException("Нет пользователя с введенными данными");
        }
        return new UserDTO(foundUser);
    }
}
