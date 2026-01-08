package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.dto.transaction.UpdateTransactionRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.*;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepo transactionRepo;
  private final UserRepo userRepo;
  private final BankAccountRepo bankAccountRepo;
  private final CategoryRepo categoryRepo;
  private final BalanceService dailyBalanceService;

  public TransactionDTO createTransaction(
      CreateTransactionRequestDTO createTransactionRequestDTO, String userUuid)
      throws UnauthorizedException, NoBankAccountFoundException, NoCategoryFoundException {
    if (createTransactionRequestDTO.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      throw new InvalidTransactionAmount("Сумма транзакции не может быть равна 0");
    }

    User user =
        userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    BankAccount bankAccount =
        bankAccountRepo
            .findByUuidAndUser_Uuid(createTransactionRequestDTO.getBankAccountUuid(), userUuid)
            .orElseThrow(() -> new NoBankAccountFoundException("Счет не найден"));

    Category category =
        categoryRepo
            .findByUuidAndUser_Uuid(createTransactionRequestDTO.getCategoryUuid(), userUuid)
            .orElseThrow(
                () ->
                    new NoCategoryFoundException(
                        "Категория не найдена или не принадлежит пользователю"));

    dailyBalanceService.updateBalance(
        bankAccount, LocalDate.now(), createTransactionRequestDTO.getAmount());

    return new TransactionDTO(
        transactionRepo.save(
            new Transaction(createTransactionRequestDTO, bankAccount, category, user)));
  }

  public List<TransactionDTO> getTransactions(String userUuid) throws UnauthorizedException {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    List<Transaction> transactions = transactionRepo.findByUser_UuidOrderByCreatedAtDesc(userUuid);

    return transactions.stream().map(TransactionDTO::new).toList();
  }

  public TransactionDTO deleteTransaction(String uuid, String userUuid)
      throws UnauthorizedException, NoTransactionFoundException {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    Transaction deleteTransaction =
        transactionRepo
            .findByUuidAndUser_Uuid(uuid, userUuid)
            .orElseThrow(() -> new NoTransactionFoundException("Нет такой транзакции"));

    transactionRepo.delete(deleteTransaction);

    if (deleteTransaction.getBankAccount() != null) {
      dailyBalanceService.updateBalance(
          deleteTransaction.getBankAccount(),
          deleteTransaction.getCreatedAt().atZone(ZoneId.of("UTC")).toLocalDate(),
          deleteTransaction.getAmount().multiply(new BigDecimal(-1)));
    }

    return new TransactionDTO(deleteTransaction);
  }

  public TransactionDTO updateTransaction(
      String uuid, UpdateTransactionRequestDTO updateTransactionRequestDTO, String userUuid) {
    userRepo.findById(userUuid).orElseThrow(() -> new UnauthorizedException("Не авторизован"));

    Transaction transaction =
        transactionRepo
            .findByUuidAndUser_Uuid(uuid, userUuid)
            .orElseThrow(
                () ->
                    new NoTransactionFoundException(
                        "Транзакция не найдена или не принадлежит пользователю"));

    if (updateTransactionRequestDTO.getAmount() != null) {
      transaction.setAmount(updateTransactionRequestDTO.getAmount());
    }

    if (updateTransactionRequestDTO.getDescription() != null) {
      transaction.setDescription(updateTransactionRequestDTO.getDescription());
    }

    if (updateTransactionRequestDTO.getCurrency() != null) {
      transaction.setCurrency(updateTransactionRequestDTO.getCurrency());
    }
    if (updateTransactionRequestDTO.getBankAccountUuid() != null) {
      BankAccount bankAccount =
          bankAccountRepo
              .findByUuidAndUser_Uuid(updateTransactionRequestDTO.getBankAccountUuid(), userUuid)
              .orElseThrow(
                  () ->
                      new NoBankAccountFoundException(
                          "Счет не найден или не принадлежит пользователю"));
      transaction.setBankAccount(bankAccount);
    }

    if (updateTransactionRequestDTO.getCategoryUuid() != null) {
      Category category =
          categoryRepo
              .findByUuidAndUser_Uuid(updateTransactionRequestDTO.getCategoryUuid(), userUuid)
              .orElseThrow(
                  () ->
                      new NoCategoryFoundException(
                          "Категория не найдена или не принадлежит пользователю"));
      transaction.setCategory(category);
    }

    transactionRepo.save(transaction);

    return new TransactionDTO(transaction);
  }
}
