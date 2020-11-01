package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private StateDao stateDao;
    @Autowired private AddressDao addressDao;
    @Autowired private CustomerAddressDao customerAddressDao;

    //Saving the address
    @Transactional(propagation= Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity)
        throws SaveAddressException {
        if(addressEntity.getActive()!=null
        && !addressEntity.getLocality().isEmpty()
        &&  !addressEntity.getCity().isEmpty()
        &&   addressEntity.getFlatBuilNo()!=null
        && !addressEntity.getFlatBuilNo().isEmpty()
        && !addressEntity.getPincode().isEmpty()
        && addressEntity.getState()!=null){
            if (!isValidPinCode(addressEntity.getPincode())) {
                throw new SaveAddressException("SAR-002","InvalidPincode");
            }
            AddressEntity createdCustomerAddress = addressDao.createCustomerAddress(addressEntity);
            CustomerAddressEntity createdCustomerAddressEntity = new CustomerAddressEntity();
            createdCustomerAddressEntity.setCustomer(customerEntity);
            customerAddressDao.createCustomerAddress(createdCustomerAddressEntity);
            return createdCustomerAddress;
        } else{
         throw new SaveAddressException("SAR-001","No field can be empty");
        }

    }
//    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity){
//        List<AddressEntity> addressEntityList =new ArrayList<>();
//        List <CustomerAddressEntity> customerAddressEntityList=addressDao.customerAddressByCustomer(customerEntity);
//        if(customerAddressEntityList!=null|| !customerAddressEntityList.isEmpty()){
//            customerAddressEntityList.forEach(
//                    customerAddressEntity=>addressEntityList.add(customerAddressEntity.getAddress()));
//            )
//        }
//        return addressEntityList;
//    }
   public AddressEntity getAddressByUUID(final String addressId, final CustomerEntity customerEntity)
       throws AuthorizationFailedException , AddressNotFoundException {
       if (addressId.isEmpty()) {
           throw new AddressNotFoundException("ANF-005", "Address id cannot be empty");
       } else {
           AddressEntity addressEntity = addressDao.getAddressByUuid(addressId);
           CustomerAddressEntity customerAddressEntity = customerAddressDao.customerAddressByAddress(addressEntity);
           if (addressEntity!=null) {
               if (!customerAddressEntity.getCustomer().getUuid().equals(customerEntity.getUuid())) {
                   throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
               }
           } else {
               throw new AddressNotFoundException("ANF-003","No address by this id");
           }
           return addressEntity;
       }
   }


            public List<StateEntity> getAllStates(){
                return stateDao.getAllStates();
            }
            public StateEntity getStateByUUID(final String stateUuid) throws AddressNotFoundException{
                if(stateDao.getStateByUUID(stateUuid)==null){
                    throw new AddressNotFoundException("ANF-002","No State by this id");
                }
                return stateDao.getStateByUUID(stateUuid);
            }

            private boolean isValidPinCode(final String pincode){
                if(pincode.length()!=6){
                    return false;
                }
                for(int i=0;i<pincode.length();i++){
                if(!Character.isDigit(pincode.charAt(i))){
                    return false;
                }
            }
            return true;
        }
   }
