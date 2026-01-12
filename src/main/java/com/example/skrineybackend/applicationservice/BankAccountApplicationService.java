package com.example.skrineybackend.applicationservice;

import com.example.skrineybackend.dto.bankaccount.BankAccountDTO;
import com.example.skrineybackend.dto.bankaccount.CreateBankAccountRequestDTO;
import com.example.skrineybackend.dto.bankaccount.UpdateBankAccountRequestDTO;
import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.service.BankAccountService;
import com.example.skrineybackend.service.CurrencyRateService;
import com.example.skrineybackend.service.TransactionService;
import com.example.skrineybackend.service.UserSettingsService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountApplicationService {
  private final BankAccountService bankAccountService;
  private final TransactionService transactionService;
  private final UserSettingsService userSettingsService;
  private final CurrencyRateService currencyRateService;

  private final UserRepo userRepo;
  private final BankAccountRepo bankAccountRepo;

  @Transactional
  public List<BankAccountDTO> getAllBankAccounts(String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts(userUuid);

    return getBankAccountsDTOWithCurrencyBalance(bankAccounts);
  }

  @Transactional
  public BankAccountDTO createBankAccount(
      String userUuid, CreateBankAccountRequestDTO requestBody) {
    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    BankAccount createdBankAccount = bankAccountService.createBankAccount(requestBody, user);

    if (!requestBody.getInitialBalance().equals(BigDecimal.ZERO)
        && requestBody.getCurrency() != null) {
      BigDecimal initialBalanceInUsd =
          currencyRateService.getAmountInUsd(
              requestBody.getInitialBalance(), requestBody.getCurrency());

      CreateTransactionRequestDTO createInitialTransactionDTO =
          new CreateTransactionRequestDTO(
              requestBody.getInitialBalance(),
              requestBody.getCurrency(),
              null,
              createdBankAccount.getUuid(),
              null);

      transactionService.createTransaction(
          createInitialTransactionDTO, createdBankAccount, null, user, initialBalanceInUsd);

      createdBankAccount.setBalanceInUsd(
          createdBankAccount.getBalanceInUsd().add(initialBalanceInUsd));
    }

    if (user.getBankAccounts().isEmpty()) {
      user.getSettings().setDefaultBankAccount(createdBankAccount);
    }

    return getBankAccountsDTOWithCurrencyBalance(createdBankAccount);
  }

  @Transactional
  public BankAccountDTO deleteBankAccount(String uuid, String userUuid) {
    BankAccount bankAccount = getBankAccountIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    userSettingsService.updateDefaultBankAccountAfterDeletion(bankAccount.getUuid());

    bankAccountService.deleteBankAccount(bankAccount);

    return getBankAccountsDTOWithCurrencyBalance(bankAccount);
  }

  @Transactional
  public BankAccountDTO updateBankAccount(
      String uuid, UpdateBankAccountRequestDTO dto, String userUuid) throws UnauthorizedException {
    BankAccount bankAccount = getBankAccountIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    BankAccount updatedBankAccount = bankAccountService.updateBankAccount(bankAccount, dto);

    return getBankAccountsDTOWithCurrencyBalance(updatedBankAccount);
  }

  private BankAccount getBankAccountIfUserAuthorizedAndOwnsIt(String uuid, String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return bankAccountRepo
        .findByUuidAndUser_Uuid(uuid, userUuid)
        .orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));
  }

  private List<BankAccountDTO> getBankAccountsDTOWithCurrencyBalance(
      List<BankAccount> bankAccounts) {
    List<BankAccountDTO> result = new ArrayList<>();

    for (BankAccount bankAccount : bankAccounts) {
      result.add(getBankAccountsDTOWithCurrencyBalance(bankAccount));
    }

    return result;
  }

  private BankAccountDTO getBankAccountsDTOWithCurrencyBalance(BankAccount bankAccount) {
    List<Transaction> transactions = bankAccount.getTransactions();
    Map<Currency, BigDecimal> currencyBalances = new HashMap<>();

    for (Transaction tx : transactions) {
      currencyBalances.merge(tx.getCurrency(), tx.getAmount(), BigDecimal::add);
    }

    return new BankAccountDTO(bankAccount, currencyBalances);
  }
}
