//package com.upgrad.FoodOrderingApp.api.controller;
//
//import com.upgrad.FoodOrderingApp.api.model.*;
//import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
//import com.upgrad.FoodOrderingApp.service.businness.ItemService;
//import com.upgrad.FoodOrderingApp.service.businness.OrderService;
//import com.upgrad.FoodOrderingApp.service.entity.*;
//import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
//import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
//import org.hibernate.internal.CriteriaImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.xml.ws.Response;
//import java.math.BigDecimal;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/")
//public class OrderController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private ItemService itemService;
//
//    @CrossOrigin
//    @RequestMapping(
//            method = RequestMethod.GET,
//            path = "/order/coupon/{coupon_name}",
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<CouponDetailsResponse> getCoupon(
//            @RequestHeader("authorization") final String accessToken,
//            @PathVariable("coupon_name") String couponName) throws AuthorizationFailedException, CouponNotFoundException {
//
//            CouponEntity couponEntity = orderService.shouldGetCouponByName(couponName,accessToken);
//
//            CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
//                    .id(UUID.fromString(couponEntity.getUuid()))
//                    .couponName(couponEntity.getCouponName())
//                    .percent(couponEntity.getPercent());
//
//            return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
//    }
//
//    @CrossOrigin
//    @RequestMapping(
//            method = RequestMethod.GET,
//            path = "/order",
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    public ResponseEntity<OrderList> getPastOrdersOfCustomer(
//            @RequestHeader("authorization") final String accessToken
//    ) throws AuthorizationFailedException {
//
//            List<OrdersEntity> ordersEntityList = orderService.shouldGetPlacedOrderDetails(accessToken);
//
//            CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
//
//            for(OrdersEntity ordersEntity : ordersEntityList) {
//
//                //fetch coupon details for order
//                OrderListCoupon orderListCoupon = new OrderListCoupon();
//                orderListCoupon.id(UUID.fromString(ordersEntity.getCoupon().getUuid()));
//                orderListCoupon.couponName(ordersEntity.getCoupon().getCouponName());
//                orderListCoupon.percent(ordersEntity.getCoupon().getPercent());
//
//                //fetch payment details for order
//                OrderListPayment orderListPayment = new OrderListPayment();
//                orderListPayment.id(UUID.fromString(ordersEntity.getPayment().getUuid()));
//                orderListPayment.paymentName(ordersEntity.getPayment().getPaymentName());
//
//                //fetch customer details for order
//                OrderListCustomer orderListCustomer = new OrderListCustomer();
//                orderListCustomer.id(UUID.fromString(ordersEntity.getCustomer().getUuid()));
//                orderListCustomer.firstName(ordersEntity.getCustomer().getFirstName());
//                orderListCustomer.lastName(ordersEntity.getCustomer().getLastName());
//                orderListCustomer.emailAddress(ordersEntity.getCustomer().getEmailAddress());
//                orderListCustomer.contactNumber(ordersEntity.getCustomer().getContactNumber());
//
//                //fetch state details from order
//                OrderListAddressState orderListAddressState = new OrderListAddressState();
//                orderListAddressState.id(UUID.fromString(ordersEntity.getAddress().getState().getUuid()));
//                orderListAddressState.stateName(ordersEntity.getAddress().getState().getStateName());
//
//                //fetch address details from order
//                OrderListAddress orderListAddress = new OrderListAddress();
//                orderListAddress.id(UUID.fromString(ordersEntity.getAddress().getUuid()));
//                orderListAddress.flatBuildingName(ordersEntity.getAddress().getFlatBuilNo());
//                orderListAddress.locality(ordersEntity.getAddress().getLocality());
//                orderListAddress.city(ordersEntity.getAddress().getCity());
//                orderListAddress.pincode(ordersEntity.getAddress().getPincode());
//                orderListAddress.state(orderListAddressState);
//
//                //The list of all orders to be arranged as per requirement
//                OrderList ordersList = new OrderList();
//
//                //fetch list of all items in a order
//                List<OrderItemEntity> listOfItemsInOrder = itemService.getItemsByOrder(ordersEntity);
//
//                for(OrderItemEntity orderItemEntity : listOfItemsInOrder) {
//
//                    //fetch each item details in order
//                    ItemQuantityResponseItem itemQuantityResponseItem =  new ItemQuantityResponseItem();
//                    itemQuantityResponseItem.id(UUID.fromString(orderItemEntity.getItem().getUuid()));
//                    itemQuantityResponseItem.itemName(orderItemEntity.getItem().getItemName());
//                    itemQuantityResponseItem.itemPrice(orderItemEntity.getItem().getPrice());
//                    itemQuantityResponseItem.type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItem().getType()));
//
//                    //fecth quantity, price of each item and add details to item_quantities array
//                    ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
//                    itemQuantityResponse.item(itemQuantityResponseItem);
//                    itemQuantityResponse.quantity(orderItemEntity.getQuantity());
//                    itemQuantityResponse.price(orderItemEntity.getPrice());
//
//                    //Add the fetched Items-list belonging to a particular order
//                    ordersList.addItemQuantitiesItem(itemQuantityResponse);
//                }
//
//                //Add the fetched details of a order to ordersList
//                ordersList.id(UUID.fromString(ordersEntity.getUuid()))
//                        .bill(new BigDecimal(ordersEntity.getBill()))
//                        .coupon(orderListCoupon)
//                        .discount(new BigDecimal(ordersEntity.getDiscount()))
//                        .date(ordersEntity.getDate().toString())
//                        .payment(orderListPayment)
//                        .customer(orderListCustomer)
//                        .address(orderListAddress);
//
//                //Add each order to customerOrderResponse
//                customerOrderResponse.addOrdersItem(ordersList);
//            }
//            return new ResponseEntity(customerOrderResponse,HttpStatus.OK);
//    }
//
//}
