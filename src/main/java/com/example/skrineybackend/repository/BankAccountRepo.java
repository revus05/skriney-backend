package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepo extends CrudRepository<BankAccount, String> {
}
