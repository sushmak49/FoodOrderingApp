package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void createCustomerAuthToken(CustomerAuthEntity customerAuthEntity){
        entityManager.persist(customerAuthEntity);
    }
    public CustomerAuthEntity getCutomerAuthByToken(final String accessToken){
        try{
            return entityManager.createNamedQuery("customerAuthByToken",customerAuthToken().class)
            entityManager.setParameter("accessToken",accessToken)
            entityManager.getSingleResult();

        } catch(NoResultException nre){
            return null;
        }
        public void updateCustomerAuth(final CustomerAuthEntity updateCustomerAuthEntity){
            entityManager.merge(updatedCustomerAuthEnity);
        }
    }

}

