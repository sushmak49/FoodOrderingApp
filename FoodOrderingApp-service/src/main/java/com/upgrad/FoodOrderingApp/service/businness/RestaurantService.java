package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;

    /**
     * Get a list of all Restaurant entitiesv
     */
    public List<RestaurantEntity> getAllRestaurant() {
        return restaurantDao.getAllRestaurant();
    }

    /**
     * Get a list of all Restaurant entities by name
     */
    public RestaurantEntity getRestaurantByName(String restaurantName) {
        RestaurantEntity restaurant = restaurantDao.getRestaurantByName(restaurantName);
        return restaurant;/** need to add exception in controller if name is null*/
    }

    /**
     * Get a list of all Restaurant entities by Category Id
     */
    public List<RestaurantEntity> getRestaurantByCategoryId(Integer categoryId) throws CategoryNotFoundException {
        List<RestaurantEntity> restaurant = restaurantDao.getRestaurantByCategoryId(categoryId);
        if (restaurant.isEmpty()) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return restaurant;
    }

    /**
     * Get a list of all Restaurant entities by UUId
     */
    public RestaurantEntity getRestaurantByUuid(String restaurantUuid) throws RestaurantNotFoundException {
        RestaurantEntity restaurant = restaurantDao.getRestaurantByUuid(restaurantUuid);
        if(restaurant==null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        return restaurant;
    }
    //Edit question content
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurant(RestaurantEntity restaurantEntity) {
        return restaurantDao.updateRestaurant(restaurantEntity);
    }
}

