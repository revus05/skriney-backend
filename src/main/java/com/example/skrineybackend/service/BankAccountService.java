package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.repository.BankAccountRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {
  private final BankAccountRepo bankAccountRepo;

  public List<BankAccount> getAllBankAccounts(String userUuid) {
    return bankAccountRepo.findAllByUser_UuidOrderByCreatedAtAsc(userUuid);
  }

  public BankAccount createBankAccount(CreateBankAccountRequestDTO requestBody, User user) {
    BankAccount bankAccount = new BankAccount(requestBody, user);

    return bankAccountRepo.save(bankAccount);
  }

  public void deleteBankAccount(BankAccount bankAccount) {
    bankAccountRepo.delete(bankAccount);
  }

  public BankAccount updateBankAccount(
      BankAccount bankAccount, UpdateBankAccountRequestDTO updateBankAccountRequestDTO) {
    if (updateBankAccountRequestDTO.getEmoji() != null) {
      bankAccount.setEmoji(updateBankAccountRequestDTO.getEmoji());
    }
    if (updateBankAccountRequestDTO.getTitle() != null) {
      bankAccount.setTitle(updateBankAccountRequestDTO.getTitle());
    }

    bankAccountRepo.save(bankAccount);

    return bankAccount;
  }
}
