package com.example.skrineybackend.applicationservice;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.dto.transaction.UpdateTransactionRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.enums.Currency;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoTransactionFoundException;
import com.example.skrineybackend.exception.UnauthorizedException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import com.example.skrineybackend.service.CurrencyRateService;
import com.example.skrineybackend.service.TransactionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionApplicationService {
  private final TransactionService transactionService;
  private final CurrencyRateService currencyRateService;

  private final UserRepo userRepo;
  private final TransactionRepo transactionRepo;
  private final BankAccountRepo bankAccountRepo;
  private final CategoryRepo categoryRepo;

  public List<TransactionDTO> getAllUserTransactions(String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return transactionService.getTransactions(userUuid).stream().map(TransactionDTO::new).toList();
  }

  @Transactional
  public TransactionDTO createTransaction(CreateTransactionRequestDTO dto, String userUuid) {
    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    BankAccount bankAccount =
        bankAccountRepo
            .findByUuidAndUser_Uuid(dto.getBankAccountUuid(), userUuid)
            .orElseThrow(() -> new NoBankAccountFoundException("Нет такого счета"));
    Category category =
        categoryRepo
            .findByUuidAndUser_Uuid(dto.getCategoryUuid(), userUuid)
            .orElseThrow(() -> new NoCategoryFoundException("Нет такой категории"));

    BigDecimal amountInUsd = currencyRateService.getAmountInUsd(dto.getAmount(), dto.getCurrency());

    Transaction createdTransaction =
        transactionService.createTransaction(dto, bankAccount, category, user, amountInUsd);

    bankAccount.setBalanceInUsd(
        bankAccount.getBalanceInUsd().add(createdTransaction.getAmountInUsd()));

    return new TransactionDTO(createdTransaction);
  }

  @Transactional
  public TransactionDTO updateTransaction(
      String uuid, UpdateTransactionRequestDTO dto, String userUuid) {
    Transaction transaction = getTransactionIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    BigDecimal oldValueInUsd =
        currencyRateService.getAmountInUsd(transaction.getAmount(), transaction.getCurrency());
    BigDecimal newValueInUsd =
        currencyRateService.getAmountInUsd(dto.getAmount(), dto.getCurrency());

    Transaction updatedTransaction = transactionService.updateTransaction(transaction, dto);

    BankAccount bankAccount = transaction.getBankAccount();

    if (transaction.getBankAccount() != null) {

      bankAccount.setBalanceInUsd(
          bankAccount
              .getBalanceInUsd()
              .add(
                  currencyRateService.getAmountInUsd(
                      oldValueInUsd.subtract(newValueInUsd).negate(), Currency.USD)));
    }

    return new TransactionDTO(updatedTransaction);
  }

  @Transactional
  public TransactionDTO deleteTransaction(String uuid, String userUuid) {
    Transaction transaction = getTransactionIfUserAuthorizedAndOwnsIt(uuid, userUuid);

    transactionService.deleteTransaction(transaction);

    BankAccount bankAccount = transaction.getBankAccount();

    if (bankAccount != null) {
      bankAccount.setBalanceInUsd(
          bankAccount
              .getBalanceInUsd()
              .add(
                  currencyRateService.getAmountInUsd(
                      transaction.getAmount().negate(), transaction.getCurrency())));
    }

    return new TransactionDTO(transaction);
  }

  private Transaction getTransactionIfUserAuthorizedAndOwnsIt(String uuid, String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    return transactionRepo
        .findByUuidAndUser_Uuid(uuid, userUuid)
        .orElseThrow(() -> new NoTransactionFoundException("Нет такой транзакции"));
  }
}
