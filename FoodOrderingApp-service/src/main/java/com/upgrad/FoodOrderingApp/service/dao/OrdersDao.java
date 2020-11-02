package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrdersDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get coupon by coupon name
    //Returns List<CouponEntity>
    public List<CouponEntity> getCouponByName(String coupon_name) {
        try {
            return (List<CouponEntity>) entityManager.createNamedQuery("getCouponByName", CustomerEntity.class).setParameter("coupon_name", coupon_name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    //Get coupon by coupon name
    //Returns Single result
    public CouponEntity getCouponByCouponName(String couponName) {
        try {
            return entityManager.createNamedQuery("couponByCouponName", CouponEntity.class).setParameter("couponName", couponName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Get coupon by coupon UUID
    public CouponEntity getCouponByCouponUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("couponByUUID", CouponEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Get orders by address entity
    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        try {
            return entityManager.createNamedQuery("ordersByAddress", OrderEntity.class).setParameter("address", addressEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Creating/Saving new order
    public OrderEntity createOrder(OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    //List all past orders of customer
    public List<OrderEntity> getOrdersByCustomers(String customerId) {
        try {
            return entityManager.createNamedQuery("ordersByCustomer", OrderEntity.class).setParameter("customerId", customerId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //List all orders by restaurant
    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            return entityManager.createNamedQuery("ordersByRestaurant", OrderEntity.class).setParameter("restaurant", restaurantEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Save a new order item in DB
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    //Save order details in DB
    public OrderEntity saveOrderDetail(OrderEntity newOrder) {
        entityManager.persist(newOrder);
        return newOrder;
    }
}