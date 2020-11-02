package com.upgrad.FoodOrderingApp.service.common;

import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;

public class Utility {
    //Utility method to help customer validate access-token format and get access-token
    public static String getAccessToken(String authorization) throws AuthorizationFailedException {
        try {
            String[] fullAccessToken = authorization.split("Bearer ");
            //Validating whether the bearer token is passed in correct format : "<Bearer accessToken>"
            if (fullAccessToken != null && fullAccessToken.length > 1) {
                String accessToken = fullAccessToken[1];
                return accessToken;
            } else {
                throw new AuthorizationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AuthorizationFailedException("ATHR-005", "Use valid authorization format <Bearer accessToken>");
        }
    }
}
