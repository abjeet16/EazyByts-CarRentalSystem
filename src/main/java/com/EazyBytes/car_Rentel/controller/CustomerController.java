package com.EazyBytes.car_Rentel.controller;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.services.Customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars(){
        List<CarDto> carDtoList = customerService.getAllCars();
        return ResponseEntity.ok(carDtoList);
    }
    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId){
        CarDto carDto = customerService.getCarById(carId);
        if (carDto!=null) return ResponseEntity.ok(carDto);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/car/book/{carId}")
    public ResponseEntity<?> BookCar(@PathVariable Long carId, @RequestBody BookCarDto bookCarDto){
        boolean success = customerService.bookCar(carId, bookCarDto);
        if (success){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/car/bookings/{userId}")
    public ResponseEntity<?> getUserCurrentBookings(@PathVariable Long userId){
        return ResponseEntity.ok(customerService.getBookingByUserId(userId));
    }
}
