package com.EazyBytes.car_Rentel.dto;


import com.EazyBytes.car_Rentel.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
}
