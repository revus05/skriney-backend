package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.DailyBalance;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.entity.UserSettings;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.repository.UserSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;
    private final UserRepo userRepo;
    private final UserSettingsRepo userSettingsRepo;

    public List<BankAccountDTO> getAllBankAccounts(String userUuid) throws UnauthorizedException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        return bankAccountRepo.findAllByUser_UuidOrderByCreatedAtAsc(userUuid).stream().map(BankAccountDTO::new).toList();
    }

    public BankAccountDTO getOneBankAccount(String userUuid, String uuid) throws UnauthorizedException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        BankAccount bankAccount = bankAccountRepo.findByUuidAndUser_Uuid(uuid, userUuid)
                .orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));

        return new BankAccountDTO(bankAccount);
    }

    public BankAccountDTO createBankAccount(CreateBankAccountRequestDTO requestBody, String userUuid) throws UnauthorizedException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));
        BankAccount bankAccount = new BankAccount(requestBody);

        if (user.getBankAccounts().isEmpty()) {
            user.getSettings().setDefaultBankAccount(bankAccount);
        }

        bankAccount.setUser(user);
        bankAccount.setDailyBalances(new ArrayList<>(Collections.singletonList(new DailyBalance(bankAccount))));

        return new BankAccountDTO(bankAccountRepo.save(bankAccount));
    }

    public BankAccountDTO deleteBankAccount(String uuid, String userUuid) throws UnauthorizedException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        BankAccount deleteBankAccount = bankAccountRepo.findByUuidAndUser_Uuid(uuid, userUuid).orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));

        List<UserSettings> settingsList = userSettingsRepo.findAllByDefaultBankAccountUuid(deleteBankAccount.getUuid());

        for (UserSettings settings : settingsList) {
            settings.setDefaultBankAccount(null);
        }

        bankAccountRepo.delete(deleteBankAccount);

        return new BankAccountDTO(deleteBankAccount);
    }

    public BankAccountDTO updateBankAccount(String uuid, UpdateBankAccountRequestDTO updateBankAccountRequestDTO, String userUuid) throws UnauthorizedException {
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

        BankAccount updateBankAccount = bankAccountRepo.findByUuidAndUser_Uuid(uuid, userUuid)
                .orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));

        if (updateBankAccountRequestDTO.getEmoji() != null) {
            updateBankAccount.setEmoji(updateBankAccountRequestDTO.getEmoji());
        }
        if (updateBankAccountRequestDTO.getTitle() != null) {
            updateBankAccount.setTitle(updateBankAccountRequestDTO.getTitle());
        }
        if (updateBankAccountRequestDTO.getCurrency() != null) {
            updateBankAccount.setCurrency(updateBankAccountRequestDTO.getCurrency());
        }

        bankAccountRepo.save(updateBankAccount);

        return new BankAccountDTO(updateBankAccount);
    }
}
