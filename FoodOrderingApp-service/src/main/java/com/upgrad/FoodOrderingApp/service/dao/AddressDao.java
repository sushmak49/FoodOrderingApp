package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class AddressDao {

    public AddressEntity createCustomerAddress(final AddressEntity addressEntity, EntityManager entityManager){
        entityManager.persist(addressEntity);
        return addressEntity;
    }
    public <CustomerEntity> List<CustomerAddressEntity> customerAddressByCustomer(CustomerEntity customer) {
        List<CustomerAddressEntity> addresses = entityManager.createNamedQuery("CustomerAddressbyCustomer"customerAddressEntity.class),
        entityManager.setParameter("addressUUID", addressuuid),
        entityManager.getSingleResult();
    } catch (NoResultException nre){
        return null;

    }
   public AddressEntity deletAddress(final AddressEntity addressEntity){
        entityManager.remove(addressEntity);
        return addressEntity;
   }
   public AddressEntity updateAddress(final AddressEnity addressEntity){
        return entityManager.merge(addressEntity);
   }
}
