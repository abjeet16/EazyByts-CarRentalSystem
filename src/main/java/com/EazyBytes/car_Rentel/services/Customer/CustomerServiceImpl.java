package com.EazyBytes.car_Rentel.services.Customer;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.dto.UserDto;
import com.EazyBytes.car_Rentel.entity.BookCar;
import com.EazyBytes.car_Rentel.enums.BookCarStatus;
import com.EazyBytes.car_Rentel.entity.Car;
import com.EazyBytes.car_Rentel.entity.User;
import com.EazyBytes.car_Rentel.repository.BookCarRepository;
import com.EazyBytes.car_Rentel.repository.CarRepository;
import com.EazyBytes.car_Rentel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BookCarRepository bookCarRepository;

    @Override
    public List<CarDto> getAllCars() {
        List<CarDto> availableCars = carRepository.findAll().stream()
                .filter(Car::getAvailable) // Assuming you have an isAvailable method
                .map(Car::getCarDto)
                .collect(Collectors.toList());
        return availableCars;
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
    public boolean bookCar(Long carID, BookCarDto bookCarDto) {
        Optional<User> optionalUser = userRepository.findById(bookCarDto.getUserID());
        Optional<Car> optionalCar = carRepository.findById(carID);

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            BookCar bookCar = new BookCar();

            // Correcting the order of date difference calculation (toDate - fromDate)
            long diffInMillSec = bookCarDto.getToDate().getTime() - bookCarDto.getFromDate().getTime();

            // Now calculate the days correctly
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillSec);

            // Setting the calculated days and amount
            bookCar.setDays(days);
            bookCar.setUser(optionalUser.get());
            bookCar.setCar(optionalCar.get());

            // Calculating the amount based on car price and days
            bookCar.setAmount(optionalCar.get().getPrice() * days);

            bookCar.setFromDate(bookCarDto.getFromDate());
            bookCar.setToDate(bookCarDto.getToDate());
            bookCar.setBookCarStatus(BookCarStatus.PENDING);

            // Save the booking
            bookCarRepository.save(bookCar);

            return true;
        }

        return false;
    }

    @Override
    public List<BookCarDto> getBookingByUserId(Long userId) {
        return bookCarRepository.findAllByUserId(userId).stream().map(BookCar::getBookCarDto).toList();
    }

    public List<UserDto> getAllCustomers() {
        return userRepository.findAll().stream()
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

}
