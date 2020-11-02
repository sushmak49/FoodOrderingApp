package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    //fetch all payment modes from DB
    public List<PaymentEntity> getAllPaymentMethods() {
        List<PaymentEntity> paymentList = entityManager.createNamedQuery("getAllPaymentModes", PaymentEntity.class).getResultList();
        return paymentList;
    }

    //fetch payment modes from DB based on UUID
    public PaymentEntity getPaymentbyUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("paymentByUUID", PaymentEntity.class)
                    .setParameter("paymentUUID", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
