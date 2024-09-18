package com.EazyBytes.car_Rentel.controller;

import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.entity.Car;
import com.EazyBytes.car_Rentel.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    /*@PostMapping("/addCar")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto){
        boolean success = adminService.postCar(carDto);
        if (success){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }*/

    // Handles form submission for adding a new car
    @PostMapping("/addCar")
    public String postCar(@ModelAttribute("car") CarDto carDto) {
        // Save the car using the service
        adminService.postCar(carDto);

        // Redirect to the car list or relevant page
        return "redirect:/api/admin/cars";  // Adjust the redirect URL as necessary
    }

    // Displays the form for adding a new car
    @GetMapping("/showFormForAddCar")
    public String showFormForAddCar(Model model) {
        // Create a model attribute to bind form data for CarDto
        CarDto carDto = new CarDto();

        // Add the CarDto object to the model
        model.addAttribute("car", carDto);

        // Return the view for the form
        return "cars/newCar";  // This should match the name of your Thymeleaf HTML file
    }

    // Read All Cars
    @GetMapping("/cars")
    public String getAllCars(Model model) {
        List<CarDto> carDtoList = adminService.getAllCars();
        model.addAttribute("cars", carDtoList);
        return "cars/car-list";  // Thymeleaf template for listing cars
    }
}
