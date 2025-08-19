package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.DeleteBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;
    private final UserRepo userRepo;

    public List<BankAccountDTO> getBankAccounts(String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        return bankAccountRepo.findAllByUser_UuidOrderByCreatedAtAsc(userUuid).stream().map(BankAccountDTO::new).toList();
    }

    public BankAccountDTO createBankAccount(CreateBankAccountRequestDTO requestBody, String userUuid) throws NoUserFoundException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount bankAccount = new BankAccount(requestBody);
        bankAccount.setUser(user);

        return new BankAccountDTO(bankAccountRepo.save(bankAccount));
    }

    public BankAccountDTO deleteBankAccount(DeleteBankAccountRequestDTO requestBody, String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount deleteBankAccount = bankAccountRepo.findByUuidAndUser_Uuid(requestBody.getUuid(), userUuid).orElseThrow(() -> new NoBankAccountFoundException("Нет такого счета у пользователя"));
        bankAccountRepo.delete(deleteBankAccount);

        return new BankAccountDTO(deleteBankAccount);
    }

    public BankAccountDTO updateBankAccount(String bankAccountUuid, UpdateBankAccountRequestDTO updateBankAccountRequestDTO, String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount updateBankAccount = bankAccountRepo.findByUuidAndUser_Uuid(bankAccountUuid, userUuid)
                .orElseThrow(() -> new NoBankAccountFoundException("Нет такого счета у пользователя"));

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
