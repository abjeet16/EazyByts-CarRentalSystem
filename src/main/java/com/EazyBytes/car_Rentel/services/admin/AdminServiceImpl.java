package com.EazyBytes.car_Rentel.services.admin;

import com.EazyBytes.car_Rentel.dto.CarDto;
import com.EazyBytes.car_Rentel.entity.Car;
import com.EazyBytes.car_Rentel.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final CarRepository carRepository;

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
}