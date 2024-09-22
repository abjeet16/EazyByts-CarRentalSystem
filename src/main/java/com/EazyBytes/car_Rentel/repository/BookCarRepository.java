package com.EazyBytes.car_Rentel.repository;

import com.EazyBytes.car_Rentel.dto.BookCarDto;
import com.EazyBytes.car_Rentel.entity.BookCar;
import com.EazyBytes.car_Rentel.enums.BookCarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCarRepository extends JpaRepository<BookCar,Long> {
    List<BookCar> findAllByUserId(Long userId);

    List<BookCar> findByBookCarStatus(BookCarStatus bookCarStatus);

    List<BookCar> findAllByBookCarStatus(BookCarStatus status);
}
