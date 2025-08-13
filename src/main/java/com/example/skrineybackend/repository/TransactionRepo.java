package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepo extends CrudRepository<Transaction, String> { }
