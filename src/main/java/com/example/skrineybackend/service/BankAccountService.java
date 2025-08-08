package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.BankAccountDTO;
import com.example.skrineybackend.dto.CreateBankAccountRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;
    private final UserRepo userRepo;

    public BankAccountService(BankAccountRepo bankAccountRepo, UserRepo userRepo) {
        this.bankAccountRepo = bankAccountRepo;
        this.userRepo = userRepo;
    }

    public BankAccountDTO createBankAccount(CreateBankAccountRequestDTO requestBody, String userUuid) throws NoUserFoundException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount bankAccount = new BankAccount(requestBody);
        bankAccount.setUser(user);

        return new BankAccountDTO(bankAccountRepo.save(bankAccount));
    }
}
