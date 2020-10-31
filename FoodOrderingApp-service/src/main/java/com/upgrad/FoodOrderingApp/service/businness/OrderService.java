package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrdersDao orderDao;

    @Autowired
    private CustomerService customerService;

    public CouponEntity shouldGetCouponByName(String couponName, final String accessToken) throws AuthorizationFailedException, CouponNotFoundException {

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        if(couponName!=null){
            CouponEntity couponEntity = orderDao.getCouponByCouponName(couponName);
            if(couponEntity!=null){
                return couponEntity;
            } else {
                throw new CouponNotFoundException("CPF-001","No coupon by this name");
            }
        } else{
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
    }

    public List<OrdersEntity> shouldGetPlacedOrderDetails(String accessToken) throws AuthorizationFailedException {

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<OrdersEntity> OrdersEntityList = orderDao.getOrdersByCustomers(customerEntity);

        return OrdersEntityList;
    }

    public CouponEntity getCouponByCouponId(String toString) {
        CouponEntity couponEntity =null;
        return couponEntity;
    }

    public OrdersEntity saveOrder(OrdersEntity newOrder) {
        return newOrder;
    }

    public void saveOrderItem(OrderItemEntity orderItemEntity) {
    }
}
