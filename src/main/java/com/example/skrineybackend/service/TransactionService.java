package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.transaction.CreateTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.DeleteTransactionRequestDTO;
import com.example.skrineybackend.dto.transaction.TransactionDTO;
import com.example.skrineybackend.entity.BankAccount;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.Transaction;
import com.example.skrineybackend.exception.NoBankAccountFoundException;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoTransactionFoundException;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.repository.BankAccountRepo;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.TransactionRepo;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;
    private final BankAccountRepo bankAccountRepo;
    private final CategoryRepo categoryRepo;

    public TransactionService(TransactionRepo transactionRepo, UserRepo userRepo, BankAccountRepo bankAccountRepo, CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.bankAccountRepo = bankAccountRepo;
        this.categoryRepo = categoryRepo;
    }

    public TransactionDTO createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO, String userUuid) throws NoUserFoundException, NoBankAccountFoundException, NoCategoryFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        BankAccount bankAccount = bankAccountRepo.findByUuidAndUser_Uuid(createTransactionRequestDTO.getBankAccountUuid(), userUuid)
                .orElseThrow(() -> new NoBankAccountFoundException("Нет такого счета у пользователя"));

        Category category = categoryRepo.findByUuidAndUser_Uuid(createTransactionRequestDTO.getCategoryUuid(), userUuid)
                .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        return new TransactionDTO(transactionRepo.save(new Transaction(createTransactionRequestDTO, bankAccount, category)));
    }

    public List<TransactionDTO> getTransactions(String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        List<Transaction> transactions = transactionRepo.findByBankAccount_User_Uuid(userUuid);

        return transactions.stream()
            .map(TransactionDTO::new)
            .toList();
    }

    public TransactionDTO deleteTransaction(DeleteTransactionRequestDTO deleteTransactionRequestDTO, String userUuid) throws NoUserFoundException, NoTransactionFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        Transaction deleteTransaction = transactionRepo.findByUuidAndBankAccount_User_Uuid(deleteTransactionRequestDTO.getUuid(), userUuid)
                .orElseThrow(() -> new NoTransactionFoundException("Нет такой транзакции"));

        transactionRepo.delete(deleteTransaction);

        return new TransactionDTO(deleteTransaction);
    }
}
