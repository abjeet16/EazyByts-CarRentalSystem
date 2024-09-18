package com.EazyBytes.car_Rentel.entity;

import com.EazyBytes.car_Rentel.dto.CarDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private String transmission;

    private String brand;

    private String type;

    private LocalDate modelYear;
    @Lob
    private String description;

    private Integer price;

    @Column(columnDefinition = "longblob")
    private byte[] image;

    public CarDto getCarDto(){
        CarDto carDto = new CarDto();
        carDto.setId(id);
        carDto.setName(name);
        carDto.setDescription(description);
        carDto.setColor(color);
        carDto.setType(type);
        carDto.setPrice(price);
        carDto.setTransmission(transmission);
        carDto.setBrand(brand);
        carDto.setModelYear(modelYear);
        carDto.setReturnedImage(image);
        return carDto;
    }
}

