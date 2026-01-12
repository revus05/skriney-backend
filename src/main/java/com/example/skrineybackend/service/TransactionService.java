package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.UpdateTransactionRequestDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.InvalidTransactionAmount;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepo transactionRepo;
  private final BankAccountRepo bankAccountRepo;
  private final CategoryRepo categoryRepo;

  public Transaction createTransaction(
      CreateTransactionRequestDTO dto,
      BankAccount bankAccount,
      Category category,
      User user,
      BigDecimal amountInUsd)
      throws InvalidTransactionAmount {
    if (dto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      throw new InvalidTransactionAmount("Сумма транзакции не может быть равна 0");
    }

    return transactionRepo.save(new Transaction(dto, bankAccount, category, user, amountInUsd));
  }

  public List<Transaction> getTransactions(String userUuid) {
    return getTransactions(userUuid, null, null);
  }

  public List<Transaction> getTransactions(
      String userUuid, @Nullable String bankAccountUuid, @Nullable Instant startDateTime) {

    if (bankAccountUuid != null && startDateTime != null) {
      return transactionRepo
          .findByUser_UuidAndBankAccount_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
              userUuid, bankAccountUuid, startDateTime);
    }

    if (bankAccountUuid != null) {
      return transactionRepo.findByUser_UuidAndBankAccount_UuidOrderByCreatedAtDesc(
          userUuid, bankAccountUuid);
    }

    if (startDateTime != null) {
      return transactionRepo.findByUser_UuidAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
          userUuid, startDateTime);
    }

    return transactionRepo.findByUser_UuidOrderByCreatedAtDesc(userUuid);
  }

  public void deleteTransaction(Transaction transaction) {
    transactionRepo.delete(transaction);
  }

  public Transaction updateTransaction(
      Transaction transaction, UpdateTransactionRequestDTO updateTransactionRequestDTO) {
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
              .findByUuidAndUser_Uuid(
                  updateTransactionRequestDTO.getBankAccountUuid(), transaction.getUser().getUuid())
              .orElseThrow(
                  () ->
                      new NoBankAccountFoundException(
                          "Счет не найден или не принадлежит пользователю"));

      transaction.setBankAccount(bankAccount);
    }

    if (updateTransactionRequestDTO.getCategoryUuid() != null) {
      Category category =
          categoryRepo
              .findByUuidAndUser_Uuid(
                  updateTransactionRequestDTO.getCategoryUuid(), transaction.getUser().getUuid())
              .orElseThrow(
                  () ->
                      new NoCategoryFoundException(
                          "Категория не найдена или не принадлежит пользователю"));
      transaction.setCategory(category);
    }

    transactionRepo.save(transaction);

    return transaction;
  }
}
