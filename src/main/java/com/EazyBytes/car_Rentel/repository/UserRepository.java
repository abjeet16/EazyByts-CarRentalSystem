package com.EazyBytes.car_Rentel.repository;

import com.EazyBytes.car_Rentel.entity.User;
import com.EazyBytes.car_Rentel.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findPersonByEmail(String email);

    User findByUserRole(UserRole userRole);
}

