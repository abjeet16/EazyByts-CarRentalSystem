package com.EazyBytes.car_Rentel.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String name;
    private String password;
    private Long phoneNumber;
}
