package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * Get a list of all Category entities
     */
    public List<CategoryEntity> getAllCategory() {
        return categoryDao.getAllCategory();
    }

    /**
     * Get a list of all Category entities by UUID
     */
    public CategoryEntity getCategoryByUuid(String categoryUuid) throws CategoryNotFoundException {
        CategoryEntity category = categoryDao.getCategoryByUuid(categoryUuid);
        if (category == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return category;
    }
}
