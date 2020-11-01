package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/payment",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentResponse> getPaymentOptions() {
        List<PaymentEntity> paymentEntityList = paymentService.getAllPaymentMethods();

        final PaymentListResponse paymentListResponses = new PaymentListResponse();

        for (PaymentEntity paymentOption : paymentEntityList) {
            String uuid = paymentOption.getUuid();
            String paymentName = paymentOption.getPaymentName();
            new PaymentResponse().id(UUID.fromString(uuid)).paymentName(paymentName);
            paymentListResponses.addPaymentMethodsItem(new PaymentResponse().id(UUID.fromString(uuid)).paymentName(paymentName));
        }

        return new ResponseEntity(paymentListResponses, HttpStatus.OK);
    }

}
