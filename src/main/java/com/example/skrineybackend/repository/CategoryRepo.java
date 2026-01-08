package com.example.skrineybackend.repository;

import com.example.skrineybackend.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends CrudRepository<Category, String> {
  List<Category> findAllByUser_UuidOrderByCreatedAt(String userUuid);

  Optional<Category> findByUuidAndUser_Uuid(String categoryUuid, String userUuid);
}
