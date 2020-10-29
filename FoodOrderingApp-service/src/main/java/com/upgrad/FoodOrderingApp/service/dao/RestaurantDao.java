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

    /**
     * A method to fetch list of all restaurants
     */
    public List<RestaurantEntity> getAllRestaurantByRating() {
        try {
        return entityManager.createNamedQuery("getAllRestaurantByRating", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * A method to get restaurant by id
     * parameter : String restaurantUUID
     * returns :  restaurantEntity
     */
    public RestaurantEntity getRestaurantByUuid(String restaurantUuid) {
        try {
            return entityManager.createNamedQuery("getRestaurantByUuid", RestaurantEntity.class)
                    .setParameter("restaurantUuid", restaurantUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }
    }

    /**
     * Method to update/Persist Restaurant details
     */
    public RestaurantEntity updateRestaurantEntity(RestaurantEntity restaurantEntity) {
        return entityManager.merge(restaurantEntity);
    }
}
