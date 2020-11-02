package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RestaurantDao restaurantDao;

    /**
     * Get a list of all Category entities
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        return categoryDao.getAllCategory();
    }

    /**
     * Get a list of all Category entities by UUID
     * @param categoryUuid
     * @return
     * @throws CategoryNotFoundException
     */
    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {
        if (categoryUuid == null || categoryUuid.equals("")) {
            CategoryEntity category = categoryDao.getCategoryByUuid(categoryUuid);
        }
        CategoryEntity category = categoryDao.getCategoryByUuid(categoryUuid);
        if (category == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return category;
    }

    /** List all categories mapped to a restaurant - list by restaurant UUID
     * @param restaurantUUID
     * @return
     */
    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUUID) {
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUUID);
        return restaurantEntity.getCategories().stream()
                .sorted(Comparator.comparing(CategoryEntity::getCategoryName))
                .collect(Collectors.toList());
    }
}
