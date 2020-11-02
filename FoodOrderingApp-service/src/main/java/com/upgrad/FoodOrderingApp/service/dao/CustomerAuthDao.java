package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext private EntityManager entityManager;

    //Persist the customer auth token in DB
    public void createCustomerAuthToken(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
    }

    /* Obtaining the accesstoken after suceessful login.*/

    public CustomerAuthEntity getCustomerAuthByToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Update customer authentication details in DB
    public void updateCustomerAuth(final CustomerAuthEntity updatedCustomerAuthEntity) {
        entityManager.merge(updatedCustomerAuthEntity);
    }
}
