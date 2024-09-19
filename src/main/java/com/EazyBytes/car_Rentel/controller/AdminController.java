package com.EazyBytes.car_Rentel.controller;

import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    // Handles form submission for adding a new car
    @PostMapping("/addCar")
    public String postCar(@ModelAttribute("car") CarDto carDto) {
        // Save the car using the service
        adminService.postCar(carDto);

        // Redirect to the car list
        return "redirect:/api/admin/cars";
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

    // Handle the deletion of a car
    @PostMapping("/cars/delete/{carID}")
    public String deleteCar(@PathVariable Long carID) {
        adminService.deleteCar(carID);
        return "redirect:/api/admin/cars";  // Redirect to the car list after deletion
    }

    // Get car by ID
    @GetMapping("/cars/getCarByID/{carID}")
    public String getCarById(@PathVariable Long carID,Model model) {
        CarDto carDtoList = adminService.getCarById(carID);
        model.addAttribute("cars", carDtoList);
        return "cars/car-list";  // Thymeleaf template for listing cars
    }
}

