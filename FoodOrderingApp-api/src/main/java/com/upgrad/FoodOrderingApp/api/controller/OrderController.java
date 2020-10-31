package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.hibernate.ResourceClosedException;
import org.hibernate.criterion.Order;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            @RequestHeader("authorization") final String accessToken,
            @PathVariable(name="coupon_name") Optional<String> couponName) throws AuthorizationFailedException, CouponNotFoundException {

        //
        CouponEntity couponEntity = orderService.shouldGetCouponByName(couponName.toString(),accessToken);

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
            @RequestHeader("authorization") final String accessToken
    ) throws AuthorizationFailedException {

            List<OrdersEntity> ordersEntityList = orderService.shouldGetPlacedOrderDetails(accessToken);

            CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();

            for(OrdersEntity ordersEntity : ordersEntityList) {

                //fetch coupon details for order
                OrderListCoupon orderListCoupon = new OrderListCoupon();
                orderListCoupon.id(UUID.fromString(ordersEntity.getCoupon().getUuid()));
                orderListCoupon.couponName(ordersEntity.getCoupon().getCouponName());
                orderListCoupon.percent(ordersEntity.getCoupon().getPercent());

                //fetch payment details for order
                OrderListPayment orderListPayment = new OrderListPayment();
                orderListPayment.id(UUID.fromString(ordersEntity.getPayment().getUuid()));
                orderListPayment.paymentName(ordersEntity.getPayment().getPaymentName());

                //fetch customer details for order
                OrderListCustomer orderListCustomer = new OrderListCustomer();
                orderListCustomer.id(UUID.fromString(ordersEntity.getCustomer().getUuid()));
                orderListCustomer.firstName(ordersEntity.getCustomer().getFirstName());
                orderListCustomer.lastName(ordersEntity.getCustomer().getLastName());
                orderListCustomer.emailAddress(ordersEntity.getCustomer().getEmailAddress());
                orderListCustomer.contactNumber(ordersEntity.getCustomer().getContactNumber());

                //fetch state details from order
                OrderListAddressState orderListAddressState = new OrderListAddressState();
                orderListAddressState.id(UUID.fromString(ordersEntity.getAddress().getState().getUuid()));
                orderListAddressState.stateName(ordersEntity.getAddress().getState().getStateName());

                //fetch address details from order
                OrderListAddress orderListAddress = new OrderListAddress();
                orderListAddress.id(UUID.fromString(ordersEntity.getAddress().getUuid()));
                orderListAddress.flatBuildingName(ordersEntity.getAddress().getFlatBuilNo());
                orderListAddress.locality(ordersEntity.getAddress().getLocality());
                orderListAddress.city(ordersEntity.getAddress().getCity());
                orderListAddress.pincode(ordersEntity.getAddress().getPincode());
                orderListAddress.state(orderListAddressState);

                //The list of all orders to be arranged as per requirement
                OrderList ordersList = new OrderList();

                //fetch list of all items in a order
                List<OrderItemEntity> listOfItemsInOrder = itemService.getItemsByOrder(ordersEntity);

                for(OrderItemEntity orderItemEntity : listOfItemsInOrder) {

                    //fetch each item details in order
                    ItemQuantityResponseItem itemQuantityResponseItem =  new ItemQuantityResponseItem();
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
                ordersList.id(UUID.fromString(ordersEntity.getUuid()))
                        .bill(new BigDecimal(ordersEntity.getBill()))
                        .coupon(orderListCoupon)
                        .discount(new BigDecimal(ordersEntity.getDiscount()))
                        .date(ordersEntity.getDate().toString())
                        .payment(orderListPayment)
                        .customer(orderListCustomer)
                        .address(orderListAddress);

                //Add each order to customerOrderResponse
                customerOrderResponse.addOrdersItem(ordersList);
            }
            return new ResponseEntity(customerOrderResponse,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/order",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SaveOrderResponse> saveOrder(
            @RequestHeader("authorization") final String accessToken,
            @RequestBody SaveOrderRequest saveOrderRequest
    ) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        //Check if the customer is logged in to create new order
         CustomerEntity customer = customerService.getCustomer(accessToken);
         CouponEntity couponEntity=null;
         AddressEntity addressEntity=null;
         PaymentEntity paymentEntity=null;
         RestaurantEntity restaurantEntity=null;
         if(saveOrderRequest.getCouponId()!=null){
             couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
         }
         if (saveOrderRequest.getAddressId()!=null) {
             addressEntity = addressService.getAddressByUuid(saveOrderRequest.getAddressId(),customer);
             if(addressEntity!=null) {
                 //check if this address belongs to the customer Id
                 //Add a method in AddressService
             }
         }
         if(saveOrderRequest.getPaymentId()!=null) {
            paymentEntity = paymentService.getPaymentByUuid(saveOrderRequest.getPaymentId());
         }
         if(saveOrderRequest.getRestaurantId()!=null){
             restaurantEntity = restaurantService.restaurantByUUID(String.valueOf(saveOrderRequest.getRestaurantId()));
         }

         //Create a final order entity
        OrdersEntity newOrder = new OrdersEntity();
         newOrder.setCustomer(customer);
         newOrder.setCoupon(couponEntity);
         newOrder.setAddress(addressEntity);
         newOrder.setPayment(paymentEntity);
         newOrder.setRestaurant(restaurantEntity);
         newOrder.setBill(saveOrderRequest.getBill().doubleValue());
         newOrder.setDiscount(saveOrderRequest.getDiscount().doubleValue());

        OrdersEntity savedOrder = orderService.saveOrder(newOrder);

         //this method also handles whether the item is present or not
         List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
         for(ItemQuantity itemQuantity:itemQuantities) {
             //check if an item is present by this Uuid
             ItemEntity item = itemService.getItemByUUID(String.valueOf(itemQuantity.getItemId()));
             OrderItemEntity orderItemEntity = new OrderItemEntity();
             orderItemEntity.setItem(item);
             orderItemEntity.setQuantity(itemQuantity.getQuantity());
             orderItemEntity.setPrice(itemQuantity.getPrice());
             //finally save the items of this order in order_item table
             orderService.saveOrderItem(orderItemEntity);

        }

         SaveOrderResponse savedOrderResponse = new SaveOrderResponse()
                 .id(savedOrder.getUuid())
                 .status("ORDER SUCCESSFULLY PLACED");

         return new ResponseEntity<SaveOrderResponse>(savedOrderResponse,HttpStatus.CREATED);
    }
}
