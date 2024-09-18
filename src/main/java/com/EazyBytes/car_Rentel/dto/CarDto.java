package com.EazyBytes.car_Rentel.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CarDto {
    private Long id;

    private String name;

    private String color;

    private String transmission;

    private String brand;

    private String type;

    private LocalDate modelYear;

    private String description;

    private Integer price;

    private MultipartFile image;

    private byte[] returnedImage;
}
