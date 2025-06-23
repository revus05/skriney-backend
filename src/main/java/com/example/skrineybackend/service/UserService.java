package com.example.skrineybackend.service;


import com.example.skrineybackend.dto.LoginRequestDTO;
import com.example.skrineybackend.dto.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exeption.InvalidCredentialsException;
import com.example.skrineybackend.exeption.InvalidEmailException;
import com.example.skrineybackend.exeption.UserAlreadyExistsException;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userService) {
        this.userRepo = userService;
    }

    public User registerUser(User user) throws UserAlreadyExistsException, InvalidEmailException {
        if (isEmailInvalid(user.getEmail())) {
            throw new InvalidEmailException("Введен некорректный email");
        }
        User foundUser = userRepo.findByEmail(user.getEmail());
        if (foundUser != null) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
        return userRepo.save(user);
    }

    public UserDTO loginUser(LoginRequestDTO loginRequestDTO) throws InvalidEmailException, InvalidCredentialsException {
        if (isEmailInvalid(loginRequestDTO.getEmail())) {
            throw new InvalidEmailException("Введен некорректный email");
        }
        User foundUser = userRepo.findByEmail(loginRequestDTO.getEmail());
        if (foundUser == null || !foundUser.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new InvalidCredentialsException("Нет пользователя с введенными данными");
        }
        return new UserDTO(foundUser);
    }

    private boolean isEmailInvalid(String email) {
        return !email.matches("^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$");
    }
}
