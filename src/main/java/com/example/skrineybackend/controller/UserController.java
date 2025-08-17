package com.example.skrineybackend.controller;

import com.example.skrineybackend.dto.response.Response;
import com.example.skrineybackend.dto.user.SignInUserRequestDTO;
import com.example.skrineybackend.dto.user.SignUpUserRequestDTO;
import com.example.skrineybackend.dto.user.UserDTO;
import com.example.skrineybackend.service.CookieService;
import com.example.skrineybackend.service.JwtService;
import com.example.skrineybackend.service.UserService;
import com.example.skrineybackend.swagger.user.GetMeOperation;
import com.example.skrineybackend.swagger.user.SignInOperation;
import com.example.skrineybackend.swagger.user.SignUpOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public UserController(UserService userService, JwtService jwtService, CookieService cookieService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @SignUpOperation
    @PostMapping("/sign-up")
    public Response signUpUser(@Valid @RequestBody SignUpUserRequestDTO signUpUserRequestDTO) {
        return new Response("Пользователь успешно создан", HttpStatus.CREATED, userService.signUpUser(signUpUserRequestDTO));
    }

    @SignInOperation
    @PostMapping("/sign-in")
    public Response signInUser(@Valid @RequestBody SignInUserRequestDTO signInUserRequestDTO, HttpServletResponse response) {
        UserDTO loggedUser = userService.signInUser(signInUserRequestDTO);
        String token = jwtService.generateToken(loggedUser.getUuid());
        cookieService.addJwtCookie(response, token);
        return new Response("Пользователь успешно авторизован", HttpStatus.OK, loggedUser);
    }

    @GetMeOperation
    @GetMapping("/me")
    public Response getMeWithJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO loggedUser = userService.getMe(((UserDetails) auth.getPrincipal()).getUsername());
        return new Response("Пользователь успешно авторизован", HttpStatus.OK, loggedUser);
    }
}