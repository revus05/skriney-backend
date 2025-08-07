package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends CrudRepository<Category, String> {
}
