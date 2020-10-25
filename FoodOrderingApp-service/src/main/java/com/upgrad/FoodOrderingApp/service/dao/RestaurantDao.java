package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    /** A method to fetch list of all restaurants */
    public List<RestaurantEntity> getAllRestaurants() {
        List<RestaurantEntity> restaurantList = entityManager.createNamedQuery("getAllRestaurants", RestaurantEntity.class).getResultList();
        return restaurantList;
    }

    /** A method to get restaurant by name
        parameter : String restaurantName
        returns : restaurantEntity
     */
    public RestaurantEntity getRestaurantByName(String restaurantName) {
        try {
            return entityManager.createNamedQuery("getRestaurantByName", RestaurantEntity.class)
                    .setParameter("restaurantName", restaurantName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    /**  A method to fetch list of all restaurants by category
        parameter : Integer categoryId
        returns : List restaurantEntity
     */
    public List<RestaurantEntity> getRestaurantByCategoryId(Integer categoryId) {
        List<RestaurantEntity> restaurantList = entityManager.createNamedQuery("getRestaurantByCategoryId", RestaurantEntity.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
        return restaurantList;
    }

    /**  A method to get restaurant by id
        parameter : Integer restaurantId
        returns :  restaurantEntity
     */
    public RestaurantEntity getRestaurantById(Integer restaurantId) {
        try {
            return entityManager.createNamedQuery("getRestaurantById", RestaurantEntity.class)
                    .setParameter("restaurantId", restaurantId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }
    /**
        Method to update/Persist Restaurant details
     */
    public RestaurantEntity updateRestaurantDetail(RestaurantEntity restaurantEntity) {
        entityManager.persist(restaurantEntity);
        return restaurantEntity;
    }
}
