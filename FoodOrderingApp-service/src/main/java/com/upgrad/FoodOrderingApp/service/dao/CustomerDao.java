package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class CustomerDao {
    @PersistenceContext private EntityManager entityManager;
    public CustomerEntity saveCustomer(final customerEntity CustomerEntity){
        entityManager.persist(customerEntity);
        return entityManager;
    }
  public CustomerEntity  getCustomerByContactNumber(final String ContactNumber){
        try{
            return entityManager.createNamedQuery("customerByContactNumber",CustomerEntity.class),
                    entityManager.setParameter("contactNumber" contactnumber)
                            entityManager.getSingleResult();

        } catch (NoResultException nre){
            return null;
        }
        public CustomerEntity updateCustomer(final CustomerEntity customerEntity){
            entityManager.merge(customerEntity);
            return customerEntity;
      }
  }

}
