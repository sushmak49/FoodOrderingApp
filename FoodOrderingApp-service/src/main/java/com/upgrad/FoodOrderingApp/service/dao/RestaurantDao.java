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
     * Return restaurant list sorted based on customer rating
     */

    public List<RestaurantEntity> getRestaurantsByRating() {
        try {
            return entityManager.createNamedQuery("getAllRestaurantByRating", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
    /**
     * A method to get restaurant by name
     * parameter : String restaurantName
     * returns : restaurantEntity
     */
    public RestaurantEntity getRestaurantByName(String restaurantName) {
        try {
            return entityManager.createNamedQuery("getRestaurantByName", RestaurantEntity.class)
                    .setParameter("restaurantName", "%" + restaurantName + "%")
                    .getSingleResult();
        } catch (NoResultException nre) {
            nre.printStackTrace();
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
