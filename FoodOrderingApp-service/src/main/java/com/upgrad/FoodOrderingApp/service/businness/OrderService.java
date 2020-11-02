package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrdersDao orderDao;

    @Autowired
    private CustomerService customerService;

    //Service method to get coupon by coupon name
    public CouponEntity getCouponByCouponName(String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        if (couponName != null || !couponName.isEmpty()) {
            CouponEntity couponEntity = orderDao.getCouponByCouponName(couponName);
            if (couponEntity != null) {
                return couponEntity;
            } else {
                throw new CouponNotFoundException("CPF-001", "No coupon by this name");
            }
        } else {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
    }

    //Service method to fetch all order based on customer logged-in
    public List<OrderEntity> getOrdersByCustomers(String customerId) throws AuthorizationFailedException {
        List<OrderEntity> orderEntityList = orderDao.getOrdersByCustomers(customerId);
        return orderEntityList;
    }

    //Validate coupon id and fetch coupon if available
    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException {
        CouponEntity couponEntity = orderDao.getCouponByCouponUUID(couponId);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        return couponEntity;
    }

    //Service method to save order of a customer
    public OrderEntity saveOrder(OrderEntity newOrder) {
        newOrder.setUuid(UUID.randomUUID().toString());
        newOrder.setDate(new Date());
        // Set the status of address to archive
        newOrder.getAddress().setActive(0);
        orderDao.saveOrderDetail(newOrder);
        return newOrder;

    }

    //Service method to save order item of an order
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        orderDao.saveOrderItem(orderItemEntity);
        return orderItemEntity;
    }
}
