package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class CustomerAddressDao<customerAddressEntity, CustomerAddressEntity> {
    @PersistenceContext private EntityManager entityManager;

    public void createCustomerAddress(final customerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
    }
public CustomerAddressEntity customerAddressbyAddress(final AddressEntity address){
        try{
            return (CustomerAddressEntity) entityManager
            .createNamedQuery("customerAddressByAddress",CustomerAddressEntity.class)
                    .setParameter("address" address)
                    .getSingleResult();
            catch (NoResultException nre){
                return null;
            }


}

}
