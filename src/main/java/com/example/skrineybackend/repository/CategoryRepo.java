package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends CrudRepository<Category, String> {
    List<Category> findAllByUser_Uuid(String userUuid);
    Optional<Category> findByUuidAndUser_Uuid(String categoryUuid, String userUuid);
}
