package com.EazyBytes.car_Rentel.dto;

import com.EazyBytes.car_Rentel.enums.BookCarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookCarDto {

    private Long Id;

    private Date fromDate;

    private Date toDate;

    private Long days;

    private Long amount;

    private BookCarStatus bookCarStatus;

    private Long userID;

    private String userName;

    private String email;
}
