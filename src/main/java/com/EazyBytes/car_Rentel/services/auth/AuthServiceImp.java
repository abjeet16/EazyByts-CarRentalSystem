package com.EazyBytes.car_Rentel.services.auth;

import com.EazyBytes.car_Rentel.dto.SignUpRequest;
import com.EazyBytes.car_Rentel.dto.UserDto;
import com.EazyBytes.car_Rentel.entity.User;
import com.EazyBytes.car_Rentel.enums.UserRole;
import com.EazyBytes.car_Rentel.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{
    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount= userRepository.findByUserRole(UserRole.ADMIN);
        if (adminAccount == null){
            User newAdminAccount = new User();
            newAdminAccount.setUserRole(UserRole.ADMIN);
            newAdminAccount.setName("Admin");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setEmail("avjeetyadav@gmail.com");
            userRepository.save(newAdminAccount);
        }
    }

    @Override
    public UserDto createCustomer(SignUpRequest signUpRequest) {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
        return userRepository.findPersonByEmail(email).isPresent();
    }

    @Override
    public boolean hasCustomerWithPhoneNumber(Long phoneNumber) {
        System.out.println(phoneNumber);
        return userRepository.findPersonByphoneNumber(phoneNumber).isPresent();
    }
}
