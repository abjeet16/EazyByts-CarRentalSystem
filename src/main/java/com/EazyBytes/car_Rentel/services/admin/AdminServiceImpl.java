package com.EazyBytes.car_Rentel.services.admin;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.entity.BookCar;
import com.EazyBytes.car_Rentel.entity.Car;
import com.EazyBytes.car_Rentel.enums.BookCarStatus;
import com.EazyBytes.car_Rentel.repository.BookCarRepository;
import com.EazyBytes.car_Rentel.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final CarRepository carRepository;

    private final BookCarRepository bookCarRepository;

    @Override
    public boolean postCar(CarDto carDto) {
        try {
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setDescription(carDto.getDescription());
            car.setType(carDto.getType());
            car.setModelYear(carDto.getModelYear()); // Ensure proper conversion if needed
            car.setTransmission(carDto.getTransmission());

            MultipartFile imageFile = carDto.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                car.setImage(imageFile.getBytes());
            }

            carRepository.save(car);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception to understand the issue
            return false;
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).toList();
    }

    @Override
    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }

    @Override
    public CarDto getCarById(Long carId) {
       Optional<Car> car=  carRepository.findById(carId);
       if (car.isPresent()){
           return car.get().getCarDto();
       }
       return null;
    }
    @Override
    // Update an existing car
    public void updateCar(Long id, CarDto carDto) {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setName(carDto.getName());
            car.setColor(carDto.getColor());
            car.setTransmission(carDto.getTransmission());
            car.setBrand(carDto.getBrand());
            car.setType(carDto.getType());
            car.setModelYear(carDto.getModelYear());
            car.setDescription(carDto.getDescription());
            car.setPrice(carDto.getPrice());
            carRepository.save(car);
        } else {
            throw new RuntimeException("Car not found");
        }
    }
    public BookCar approveBooking(Long bookingId) {
        BookCar booking = bookCarRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        booking.getCar().setAvailable(false);
        booking.setBookCarStatus(BookCarStatus.APPROVED);
        return bookCarRepository.save(booking);
    }

    public BookCar rejectBooking(Long bookingId) {
        BookCar booking = bookCarRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        booking.getCar().setAvailable(true);
        booking.setBookCarStatus(BookCarStatus.REJECTED);
        return bookCarRepository.save(booking);
    }

    @Override
    public List<BookCarDto> getAllPendingBookings() {
        List<BookCar> pendingBookings = bookCarRepository.findByBookCarStatus(BookCarStatus.PENDING);
        return pendingBookings.stream()
                .map(BookCar::getBookCarDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookCarDto> getApprovedBookings() {
        return bookCarRepository.findAllByBookCarStatus(BookCarStatus.APPROVED)
                .stream()
                .map(booking -> {
                    BookCarDto dto = new BookCarDto();
                    // Map booking entity to DTO (e.g., id, userName, email, phoneNumber, etc.)
                    dto.setId(booking.getId());
                    dto.setUserName(booking.getUser().getName());
                    dto.setEmail(booking.getUser().getEmail());
                    dto.setPhoneNumber(booking.getUser().getPhoneNumber());
                    dto.setFromDate(booking.getFromDate());
                    dto.setToDate(booking.getToDate());
                    dto.setAmount(booking.getAmount());
                    dto.setBookCarStatus(booking.getBookCarStatus());
                    return dto;
                }).collect(Collectors.toList());
    }
}