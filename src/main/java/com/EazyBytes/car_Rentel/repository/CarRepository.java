package com.EazyBytes.car_Rentel.repository;

import com.EazyBytes.car_Rentel.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
}
