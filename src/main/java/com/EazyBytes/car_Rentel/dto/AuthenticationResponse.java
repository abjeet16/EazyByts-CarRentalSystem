package com.EazyBytes.car_Rentel.dto;

import com.EazyBytes.car_Rentel.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private Long userID;
}
