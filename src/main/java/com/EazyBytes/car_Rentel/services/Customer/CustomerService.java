package com.EazyBytes.car_Rentel.services.Customer;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.dto.UserDto;

import java.util.List;

public interface CustomerService {
    List<CarDto> getAllCars();

    CarDto getCarById(Long carId);

    boolean bookCar(Long carID ,  BookCarDto bookCarDto);

    List<BookCarDto> getBookingByUserId(Long userId);

    public List<UserDto> getAllCustomers();
}
