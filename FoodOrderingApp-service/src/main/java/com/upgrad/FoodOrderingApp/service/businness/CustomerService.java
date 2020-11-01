package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired private CustomerDao customerDao;

    @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired private CustomerAuthDao customerAuthDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity)
            throws SignUpRestrictedException {

        if (!customerEntity.getFirstName().isEmpty()
                && !customerEntity.getEmailAddress().isEmpty()
                && !customerEntity.getContactNumber().isEmpty()
                && !customerEntity.getPassword().isEmpty()) {
            if (isContactNumberInUse(customerEntity.getContactNumber())) {
                throw new SignUpRestrictedException(
                        "SGR-001", "This contact number is already registered! Try other contact number.");
            }
            if (!isValidEmail(customerEntity.getEmailAddress())) {
                throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
            }
            if (!isValidContactNumber(customerEntity.getContactNumber())) {
                throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
            }
            if (!isValidPassword(customerEntity.getPassword())) {
                throw new SignUpRestrictedException("SGR-004", "Weak password!");
            }
            customerEntity.setUuid(UUID.randomUUID().toString());
            // encrypts salt and password
            String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return customerDao.saveCustomer(customerEntity);
        } else {
            throw new SignUpRestrictedException(
                    "SGR-005", "Except last name all fields should be filled");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String username, String password)
            throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(username);
        if (customerEntity == null) {
            throw new AuthenticationFailedException(
                    "ATH-001", "This contact number has not been registered!");
        }
        final String encryptedPassword =
                PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (!encryptedPassword.equals(customerEntity.getPassword())) {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
        customerAuthEntity.setUuid(UUID.randomUUID().toString());
        customerAuthEntity.setCustomer(customerEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        customerAuthEntity.setLoginAt(now);
        customerAuthEntity.setExpiresAt(expiresAt);
        String accessToken = jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt);
        customerAuthEntity.setAccessToken(accessToken);

        customerAuthDao.createCustomerAuthToken(customerAuthEntity);
        return customerAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(
            final String accessToken)
            throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByToken(accessToken);
        CustomerEntity customerEntity = getCustomer(accessToken);
        customerAuthEntity.setCustomer(customerEntity);
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        customerAuthDao.updateCustomerAuth(customerAuthEntity);
        return customerAuthEntity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) {
        return customerDao.updateCustomer(customerEntity);
    }


    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByToken(accessToken);
        if (customerAuthEntity != null) {
            if (customerAuthEntity.getLogoutAt() != null) {
                throw new AuthorizationFailedException(
                        "ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }
            if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
                throw new AuthorizationFailedException(
                        "ATHR-003", "Your session is expired. Log in again to access this endpoint.");
            }
                return (CustomerEntity) customerAuthEntity.getCustomer();
        } else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
//        return customerAuthEntity.getCustomer();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(
            final String oldPassword, final String newPassword, final CustomerEntity customerEntity)
            throws UpdateCustomerException {
        if (isValidPassword(newPassword)) {
            String oldEncryptedPassword =
                    PasswordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
            if (!oldEncryptedPassword.equals(customerEntity.getPassword())) {
                throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
            }
            String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return customerDao.updateCustomer(customerEntity);
        } else {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
    }

    private boolean isContactNumberInUse(final String contactNumber) {
        return (customerDao.getCustomerByContactNumber(contactNumber) != null);
    }

    private boolean isValidEmail(final String emailAddress) {
        String regex = "[a-zA-Z0-9][a-zA-Z0-9]+@[a-zA-Z0-9][a-zA-Z0-9]+[.][a-zA-Z0-9][a-zA-Z0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailAddress);
        if (matcher.matches()){
            return true;
        } else {

            return false;
        }
    }


    private boolean isValidContactNumber(final String contactNumber) {
        if (contactNumber.length() != 10) {
            return false;
        }
        for (int i = 0; i < contactNumber.length(); i++) {
            if (!Character.isDigit(contactNumber.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPassword(final String password) {
        return password.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^]).{8,}$");
    }
}
