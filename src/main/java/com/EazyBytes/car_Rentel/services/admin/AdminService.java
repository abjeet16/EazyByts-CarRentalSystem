package com.EazyBytes.car_Rentel.services.admin;

import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.entity.BookCar;
import com.EazyBytes.car_Rentel.entity.Car;

import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto);

    List<CarDto> getAllCars();

    void deleteCar(Long carId);

    CarDto getCarById(Long carId);

    void updateCar(Long id, CarDto carDto);

    BookCar approveBooking(Long bookingId);

    BookCar rejectBooking(Long bookingId);
}
