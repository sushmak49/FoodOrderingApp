package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * A method to fetch list of all category
     */
    public List<CategoryEntity> getAllCategory() {
        List<CategoryEntity> categoryEntityList = entityManager.createNamedQuery("getAllRestaurant", CategoryEntity.class).getResultList();
        return categoryEntityList;
    }

    /**
     * A method to fetch category by id
     */
    public CategoryEntity getCategoryById(Integer categoryId) {
        try {
            return entityManager.createNamedQuery("getCategoryById", CategoryEntity.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

}
