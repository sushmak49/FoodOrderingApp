package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.Utility;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired private CustomerService customerService;

    @Autowired private AddressService addressService;


    /**
     * This api endpoint is used to save address of a customer in the database.
     *
     * @param authorization customer login access token in 'Bearer <access-token>' format.
     * @return ResponseEntity<SaveAddressResponse> type object along with HttpStatus as Ok.
     * @throws AuthorizationFailedException if any of the validation on customer access token fails.
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
            @RequestHeader("authorization") final String authorization,
            @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
            throws SaveAddressException, AuthorizationFailedException, AddressNotFoundException {

        final String accessToken = Utility.getAccessToken(authorization);
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        AddressEntity addressEntity = new AddressEntity();
        if (saveAddressRequest != null) {
            addressEntity.setUuid(UUID.randomUUID().toString());
            addressEntity.setCity(saveAddressRequest.getCity());
            addressEntity.setLocality(saveAddressRequest.getLocality());
            addressEntity.setPincode(saveAddressRequest.getPincode());
            addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
            addressEntity.setActive(1);
        }
        addressEntity.setState(addressService.getStateByUUID(saveAddressRequest.getStateUuid()));

        final AddressEntity savedAddress = addressService.saveAddress(addressEntity, customerEntity);

        SaveAddressResponse saveAddressResponse =
                new SaveAddressResponse()
                        .id(savedAddress.getUuid())
                        .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * This api endpoint is used retrieves all the saved addresses of a customer from the database.    */

    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/address/customer",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        final String accessToken = Utility.getAccessToken(authorization);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        final List<AddressEntity> addressEntityList = addressService.getAllAddress(customerEntity);

        final AddressListResponse addressListResponse = new AddressListResponse();

        if (!addressEntityList.isEmpty()) {
            for (AddressEntity addressEntity : addressEntityList) {
                AddressList addressResponseList =
                        new AddressList()
                                .id(UUID.fromString(addressEntity.getUuid()))
                                .flatBuildingName(addressEntity.getFlatBuilNo())
                                .city(addressEntity.getCity())
                                .pincode(addressEntity.getPincode())
                                .locality(addressEntity.getLocality())
                                .state(
                                        new AddressListState()
                                                .id(UUID.fromString(addressEntity.getState().getUuid()))
                                                .stateName(addressEntity.getState().getStateName()));
                addressListResponse.addAddressesItem(addressResponseList);
            }
        } else {
            List<AddressList> addresses = Collections.emptyList();
            addressListResponse.addresses(addresses);
        }

        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

//Delete customer address from database
@CrossOrigin
@RequestMapping(
        path = "/address/{address_id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<DeleteAddressResponse> deleteAddress(
        @RequestHeader("authorization") final String authorization,
        @PathVariable("address_id") final String addressId)
        throws AuthorizationFailedException, AddressNotFoundException {
    final String accessToken = Utility.getAccessToken(authorization);
    final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
    AddressEntity addressEntity = addressService.getAddressByUUID(addressId, customerEntity);
    final AddressEntity deletedAddressEntity = new AddressEntity();
    deletedAddressEntity.setUuid(UUID.randomUUID().toString());
    final AddressEntity deleteAddress = addressService.deleteAddress(addressEntity);
    final DeleteAddressResponse deleteAddressResponse =
            new DeleteAddressResponse()
                    .id(UUID.fromString(deleteAddress.getUuid()))
                    .status("ADDRESS DELETED SUCCESSFULLY");
    return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
}

    /**
     * Endpoint to fetch all states in database table state
     * @return
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/states")
    public ResponseEntity<StatesListResponse> getAllStates() {
        final StateEntity stateEntity = new StateEntity();
        stateEntity.setUuid(UUID.randomUUID().toString());

        final List<StateEntity> statesLists = addressService.getAllStates();

        final StatesListResponse statesListResponse = new StatesListResponse();
        for (StateEntity statesEntity : statesLists) {
            StatesList states =
                    new StatesList()
                            .id(UUID.fromString(statesEntity.getUuid()))
                            .stateName(statesEntity.getStateName());
            statesListResponse.addStatesItem(states);
        }
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }
}


