package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends CrudRepository<Account, String> {
}
