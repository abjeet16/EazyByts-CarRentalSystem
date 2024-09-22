package com.EazyBytes.car_Rentel.services.auth;

import com.EazyBytes.car_Rentel.dto.SignUpRequest;
import com.EazyBytes.car_Rentel.dto.UserDto;

public interface AuthService {
    UserDto createCustomer(SignUpRequest signUpRequest);

    boolean hasCustomerWithEmail(String email);

    boolean hasCustomerWithPhoneNumber(Long phoneNumber);
}
