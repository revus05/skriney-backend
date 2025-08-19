package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends CrudRepository<Transaction, String> {
    List<Transaction> findByBankAccount_User_UuidOrderByCreatedAtDesc(String userUuid);
    Optional<Transaction> findByUuidAndBankAccount_User_Uuid(String uuid, String userUuid);
}
