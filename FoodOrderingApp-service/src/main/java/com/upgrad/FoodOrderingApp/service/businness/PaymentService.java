package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public List<PaymentEntity> getAllPaymentMethods(){
        List<PaymentEntity> paymentMethodsList = paymentDao.getAllPaymentMethods();
        return paymentMethodsList;
    }

    public PaymentEntity getPaymentByUuid(UUID paymentId) {
        PaymentEntity paymentEntity=null;
        return paymentEntity;
    }
}
