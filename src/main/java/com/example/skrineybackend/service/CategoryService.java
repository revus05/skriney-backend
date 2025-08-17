package com.example.skrineybackend.service;

import com.example.skrineybackend.dto.category.CategoryDTO;
import com.example.skrineybackend.dto.category.CreateCategoryRequestDTO;
import com.example.skrineybackend.dto.category.DeleteCategoryRequestDTO;
import com.example.skrineybackend.entity.Category;
import com.example.skrineybackend.entity.User;
import com.example.skrineybackend.exception.NoCategoryFoundException;
import com.example.skrineybackend.exception.NoUserFoundException;
import com.example.skrineybackend.repository.CategoryRepo;
import com.example.skrineybackend.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    public CategoryService(CategoryRepo categoryRepo, UserRepo userRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    public CategoryDTO createCategory(CreateCategoryRequestDTO createCategoryRequestDTO, String userUuid) throws NoUserFoundException {
        User user = userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        Category category = new Category(createCategoryRequestDTO);
        category.setUser(user);

        return new CategoryDTO(categoryRepo.save(category));
    }

    public List<CategoryDTO> getCategories(String userUuid) throws NoUserFoundException {
        userRepo.findById(userUuid).orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        return categoryRepo.findAllByUser_Uuid(userUuid).stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO deleteCategory(DeleteCategoryRequestDTO deleteCategoryRequestDTO, String userUuid) throws NoUserFoundException, NoCategoryFoundException {
        userRepo.findById(userUuid)
                .orElseThrow(() -> new NoUserFoundException("Не авторизован"));

        Category category = categoryRepo.findByUuidAndUser_Uuid(deleteCategoryRequestDTO.getUuid(), userUuid)
                .orElseThrow(() -> new NoCategoryFoundException("Категория не найдена или не принадлежит пользователю"));

        categoryRepo.delete(category);

        return new CategoryDTO(category);
    }

}
