package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepo extends CrudRepository<BankAccount, String> {
    Optional<BankAccount> findByUuidAndUser_Uuid(String uuid, String userUuid);
    List<BankAccount> findAllByUser_UuidOrderByCreatedAtAsc(String userUuid);
}
