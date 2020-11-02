package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    //Service method to get all payment modes
    public List<PaymentEntity> getAllPaymentMethods() {
        List<PaymentEntity> paymentMethodsList = paymentDao.getAllPaymentMethods();
        return paymentMethodsList;
    }

    //Service method to fetch payment modes based on payment UUID
    public PaymentEntity getPaymentByUUID(String paymentId) throws PaymentMethodNotFoundException {
        PaymentEntity paymentEntity = paymentDao.getPaymentbyUuid(paymentId);
        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        }
        return paymentEntity;
    }
}
