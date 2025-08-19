package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.DeleteTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.exception.*;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;
    private final BankAccountRepo bankAccountRepo;
    private final CategoryRepo categoryRepo;
    private final BalanceService dailyBalanceService;

    public TransactionDTO createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, String userUuid) throws NoUserFoundException, NoBankAccountFoundException, NoCategoryFoundException {
        if (createTransactionRequestDTO.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidTransactionAmount("Сумма транзакции не может быть равна 0");
        }

        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount bankAccount = bankAccountRepo.findByUuidAndUser_Uuid(createTransactionRequestDTO.getBankAccountUuid(), userUuid)
                .orElseThrow(() -> new NoBankAccountFoundException("Нет такого счета у пользователя"));

        Category category = categoryRepo.findByUuidAndUser_Uuid(createTransactionRequestDTO.getCategoryUuid(), userUuid)
                .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        dailyBalanceService.updateBalance(bankAccount, LocalDate.now(), createTransactionRequestDTO.getAmount());

        return new TransactionDTO(transactionRepo.save(new Transaction(createTransactionRequestDTO, bankAccount, category)));
    }

    public List<TransactionDTO> getTransactions(String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        List<Transaction> transactions = transactionRepo.findByBankAccount_User_UuidOrderByCreatedAtDesc(userUuid);

        return transactions.stream()
            .map(TransactionDTO::new)
            .toList();
    }

    public TransactionDTO deleteTransaction(DeleteTransactionRequestDTO deleteTransactionRequestDTO, String userUuid) throws NoUserFoundException, NoTransactionFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        Transaction deleteTransaction = transactionRepo.findByUuidAndBankAccount_User_Uuid(deleteTransactionRequestDTO.getUuid(), userUuid)
                .orElseThrow(() -> new NoTransactionFoundException("Нет такой транзакции"));

        transactionRepo.delete(deleteTransaction);

        dailyBalanceService.updateBalance(deleteTransaction.getBankAccount(), deleteTransaction.getCreatedAt().atZone(ZoneId.of("UTC")).toLocalDate(), deleteTransaction.getAmount().multiply(new BigDecimal(-1)));

        return new TransactionDTO(deleteTransaction);
    }
}
