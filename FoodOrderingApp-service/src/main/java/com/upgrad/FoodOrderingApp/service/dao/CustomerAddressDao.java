package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**customer and address controllers for database access */

@Repository
public class CustomerAddressDao {

    @PersistenceContext private EntityManager entityManager;

    //Creating an entry in customer_address table
    public void createCustomerAddress(final CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
    }

    /*getting back the  address of a customer using given address.*/
    public CustomerAddressEntity customerAddressByAddress(final AddressEntity address) {
        try {
            return entityManager
                    .createNamedQuery("customerAddressByAddress", CustomerAddressEntity.class)
                    .setParameter("address", address)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}