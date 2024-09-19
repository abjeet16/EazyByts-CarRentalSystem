package com.EazyBytes.car_Rentel.controller;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.dto.UserDto;
import com.EazyBytes.car_Rentel.entity.BookCar;
import com.EazyBytes.car_Rentel.services.Customer.CustomerService;
import com.EazyBytes.car_Rentel.services.admin.AdminService;
import com.EazyBytes.car_Rentel.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    private final CustomerService customerService;


    @GetMapping("/dashBoard")
    public String getDashBoard(Model model) {
        List<CarDto> carDtoList = adminService.getAllCars();
        model.addAttribute("cars", carDtoList);
        return "dashboard/admin-dashboard";  // Thymeleaf template for listing cars
    }

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

    // Get Car by ID
    @GetMapping("/cars/getCarByID")
    public String getCarByID(@RequestParam("carID") Long carID, Model model) {
        CarDto carDto = adminService.getCarById(carID);
        if (carDto != null) {
            model.addAttribute("car", carDto);
            return "cars/car-details";  // Create this template to display car details
        } else {
            model.addAttribute("error", "Car not found");
            return "cars/car-list";  // Redirect back to the car list if not found
        }
    }
    // Displays the form for editing an existing car
    @GetMapping("/cars/edit/{carID}")
    public String showFormForEditCar(@PathVariable("carID") Long carID, Model model) {
        CarDto carDto = adminService.getCarById(carID);
        if (carDto != null) {
            model.addAttribute("car", carDto);
            return "cars/editCar";  // This should match the name of your Thymeleaf HTML file for editing
        } else {
            return "redirect:/api/admin/cars";  // Redirect back to the car list if the car is not found
        }
    }

    // Handles form submission for updating an existing car
    @PostMapping("/updateCar/{carID}")
    public String updateCar(@PathVariable("carID") Long carID, @ModelAttribute("car") CarDto carDto) {
        adminService.updateCar(carID, carDto);
        return "redirect:/api/admin/cars";
    }

    // See all customers
    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        List<UserDto> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "dashboard/see-customers";
    }

    // Endpoint to approve a booking
    @PutMapping("/booking/approve/{bookingId}")
    public ResponseEntity<BookCarDto> approveBooking(@PathVariable Long bookingId) {
        BookCar booking = adminService.approveBooking(bookingId);
        return ResponseEntity.ok(booking.getBookCarDto());
    }

    // Endpoint to reject a booking
    @PutMapping("/booking/reject/{bookingId}")
    public ResponseEntity<BookCarDto> rejectBooking(@PathVariable Long bookingId) {
        BookCar booking = adminService.rejectBooking(bookingId);
        return ResponseEntity.ok(booking.getBookCarDto());
    }

    @GetMapping("/booking/requests")
    public String getAllBookingRequests(Model model) {
        List<BookCarDto> bookingRequests = adminService.getAllPendingBookings();
        model.addAttribute("bookings", bookingRequests);
        return "dashboard/booking-request";
    }
}

