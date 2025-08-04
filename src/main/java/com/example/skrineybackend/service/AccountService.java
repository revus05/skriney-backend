package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.AccountDTO;
import com.example.skrineybackend.dto.CreateAccountRequestDTO;
import com.example.skrineybackend.entity.Account;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.repository.AccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    public AccountService(AccountRepo accountRepo, UserRepo userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public AccountDTO createAccount(CreateAccountRequestDTO requestBody, String userUuid) throws NoUserFoundException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        Account account = new Account(requestBody);
        account.setUser(user);

        return new AccountDTO(accountRepo.save(account));
    }
}
