package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.user.SignInUserRequestDTO;
import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.example.skrineybackend.dto.user.UpdateUserImageRequestDTO;
import com.example.skrineybackend.dto.user.UserDTO;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.exception.UserAlreadyExistsException;
import com.example.skrineybackend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public UserDTO signUpUser(SignUpUserRequestDTO signUpUserRequestDTO)
      throws UserAlreadyExistsException {
    checkUserExists(signUpUserRequestDTO.getEmail(), signUpUserRequestDTO.getUsername());

    String encodedPassword = passwordEncoder.encode(signUpUserRequestDTO.getPassword());
    signUpUserRequestDTO.setPassword(encodedPassword);

    return new UserDTO(userRepo.save(new User(signUpUserRequestDTO)));
  }

  public UserDTO signInUser(SignInUserRequestDTO signInUserRequestDTO)
      throws UnauthorizedException {
    User foundUser =
        userRepo
            .findByEmail(signInUserRequestDTO.getEmail())
            .filter(
                user ->
                    passwordEncoder.matches(signInUserRequestDTO.getPassword(), user.getPassword()))
            .orElseThrow(() -> new UnauthorizedException("Нет пользователя с введенными данными"));

    return new UserDTO(foundUser);
  }

  public UserDTO getMe(String userUuid) throws UnauthorizedException {
    User foundUser =
        userRepo
            .findById(userUuid)
            .orElseThrow(() -> new UnauthorizedException("Нет пользователя для такого uuid"));

    return new UserDTO(foundUser);
  }

  private void checkUserExists(String email, String username) {
    userRepo
        .findByUsername(username)
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException("username", "Имя пользователя занято");
            });

    userRepo
        .findByEmail(email)
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException(
                  "email", "Пользователь с таким email уже существует");
            });
  }

  public UserDTO updateImage(UpdateUserImageRequestDTO updateUserImageRequestDTO, String userUuid) {
    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    user.setImage(updateUserImageRequestDTO.getImage());
    userRepo.save(user);

    return new UserDTO(user);
  }

  public String connectTelegram(long telegramId, String userUuid) throws UnauthorizedException {
    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    user.setTelegramId(telegramId);

    userRepo.save(user);

    return user.getUsername();
  }

  public User getUserByTelegramId(long telegramId) throws UnauthorizedException {
    return userRepo
        .findByTelegramId(telegramId)
        .orElseThrow(() -> new UnauthorizedException("Не авторизован"));
  }
}
