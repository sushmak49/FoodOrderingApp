package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.common.Utility;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestaurantService restaurantService;

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/order/coupon/{coupon_name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(
            @RequestHeader("authorization") final String authorization,
            @PathVariable(name = "coupon_name", required = false) String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        //Check valid authorization format <Bearer accessToken>
        String accessToken = Utility.getAccessToken(authorization);

        //Authorize the customer and fetch coupon details by coupon name
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName.toString());

        //Response entity
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                .id(UUID.fromString(couponEntity.getUuid()))
                .couponName(couponEntity.getCouponName())
                .percent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/order",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<OrderList> getPastOrdersOfCustomer(
            @RequestHeader("authorization") final String authorization
    ) throws AuthorizationFailedException {

        //Check valid authorization format <Bearer accessToken>
        String accessToken = Utility.getAccessToken(authorization);

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<OrderEntity> orderEntityList = orderService.getOrdersByCustomers(customerEntity.getUuid());

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

        for (OrderEntity orderEntity : orderEntityList) {

            //fetch coupon details for order
            OrderListCoupon orderListCoupon = new OrderListCoupon();
            orderListCoupon.id(UUID.fromString(orderEntity.getCoupon().getUuid()));
            orderListCoupon.couponName(orderEntity.getCoupon().getCouponName());
            orderListCoupon.percent(orderEntity.getCoupon().getPercent());

            //fetch payment details for order
            OrderListPayment orderListPayment = new OrderListPayment();
            orderListPayment.id(UUID.fromString(orderEntity.getPayment().getUuid()));
            orderListPayment.paymentName(orderEntity.getPayment().getPaymentName());

            //fetch customer details for order
            OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.id(UUID.fromString(orderEntity.getCustomer().getUuid()));
            orderListCustomer.firstName(orderEntity.getCustomer().getFirstName());
            orderListCustomer.lastName(orderEntity.getCustomer().getLastName());
            orderListCustomer.emailAddress(orderEntity.getCustomer().getEmailAddress());
            orderListCustomer.contactNumber(orderEntity.getCustomer().getContactNumber());

            //fetch state details from order
            OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListAddressState.id(UUID.fromString(orderEntity.getAddress().getState().getUuid()));
            orderListAddressState.stateName(orderEntity.getAddress().getState().getStateName());

            //fetch address details from order
            OrderListAddress orderListAddress = new OrderListAddress();
            orderListAddress.id(UUID.fromString(orderEntity.getAddress().getUuid()));
            orderListAddress.flatBuildingName(orderEntity.getAddress().getFlatBuilNo());
            orderListAddress.locality(orderEntity.getAddress().getLocality());
            orderListAddress.city(orderEntity.getAddress().getCity());
            orderListAddress.pincode(orderEntity.getAddress().getPincode());
            orderListAddress.state(orderListAddressState);

            //The list of all orders to be arranged as per requirement
            OrderList ordersList = new OrderList();

            //fetch list of all items in a order
            List<OrderItemEntity> listOfItemsInOrder = itemService.getItemsByOrder(orderEntity);

            for (OrderItemEntity orderItemEntity : listOfItemsInOrder) {

                //fetch each item details in order
                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                itemQuantityResponseItem.id(UUID.fromString(orderItemEntity.getItem().getUuid()));
                itemQuantityResponseItem.itemName(orderItemEntity.getItem().getItemName());
                itemQuantityResponseItem.itemPrice(orderItemEntity.getItem().getPrice());
                itemQuantityResponseItem.type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItem().getType().getValue()));

                //fetch quantity, price of each item and add details to item_quantities array
                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
                itemQuantityResponse.item(itemQuantityResponseItem);
                itemQuantityResponse.quantity(orderItemEntity.getQuantity());
                itemQuantityResponse.price(orderItemEntity.getPrice());

                //Add the fetched Items-list belonging to a particular order
                ordersList.addItemQuantitiesItem(itemQuantityResponse);
            }

            //Add the fetched details of a order to ordersList
            ordersList.id(UUID.fromString(orderEntity.getUuid()))
                    .bill(new BigDecimal(orderEntity.getBill()))
                    .coupon(orderListCoupon)
                    .discount(new BigDecimal(orderEntity.getDiscount()))
                    .date(orderEntity.getDate().toString())
                    .payment(orderListPayment)
                    .customer(orderListCustomer)
                    .address(orderListAddress);

            //Add each order to customerOrderResponse
            customerOrderResponse.addOrdersItem(ordersList);
        }
        return new ResponseEntity(customerOrderResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/order",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SaveOrderResponse> saveOrder(
            @RequestHeader("authorization") final String authorization,
            @RequestBody SaveOrderRequest saveOrderRequest
    ) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        //Check valid authorization format <Bearer accessToken>
        String accessToken = Utility.getAccessToken(authorization);

        //Check if the customer is logged in to create new order
        CustomerEntity customer = customerService.getCustomer(accessToken);
        CouponEntity couponEntity = null;
        AddressEntity addressEntity = null;
        PaymentEntity paymentEntity = null;
        RestaurantEntity restaurantEntity = null;
        if (saveOrderRequest.getCouponId() != null) {
            couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
        }
        if (saveOrderRequest.getPaymentId() != null) {
            paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        }
        if (saveOrderRequest.getAddressId() != null) {
            //Validating if addressId is present and belongs to logged-in customer
            addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customer);
        }
        if (saveOrderRequest.getRestaurantId() != null) {
            restaurantEntity = restaurantService.restaurantByUUID(String.valueOf(saveOrderRequest.getRestaurantId()));
        }

        //Create a final order entity
        OrderEntity newOrder = new OrderEntity();
        newOrder.setCustomer(customer);
        newOrder.setCoupon(couponEntity);
        newOrder.setAddress(addressEntity);
        newOrder.setPayment(paymentEntity);
        newOrder.setRestaurant(restaurantEntity);
        newOrder.setBill(saveOrderRequest.getBill().doubleValue());
        newOrder.setDiscount(saveOrderRequest.getDiscount().doubleValue());

        OrderEntity savedOrder = orderService.saveOrder(newOrder);

        //this method also handles whether the item is present or not
        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
        for (ItemQuantity itemQuantity : itemQuantities) {
            //check if an item is present by this Uuid
            ItemEntity item = itemService.getItemByUUID(String.valueOf(itemQuantity.getItemId()));
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setItem(item);
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setOrder(savedOrder);
            //finally save the items of this order in order_item table
            orderService.saveOrderItem(orderItemEntity);

        }

        SaveOrderResponse savedOrderResponse = new SaveOrderResponse()
                .id(savedOrder.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<SaveOrderResponse>(savedOrderResponse, HttpStatus.CREATED);
    }
}
