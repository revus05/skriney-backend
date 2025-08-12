package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends CrudRepository<Category, String> {
    List<Category> findAllByUser_Uuid(String userUuid);
}
