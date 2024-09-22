package com.EazyBytes.car_Rentel.controller;

import com.EazyBytes.car_Rentel.dto.AuthenticationRequest;
import com.EazyBytes.car_Rentel.dto.AuthenticationResponse;
import com.EazyBytes.car_Rentel.dto.SignUpRequest;
import com.EazyBytes.car_Rentel.dto.UserDto;
import com.EazyBytes.car_Rentel.entity.User;
import com.EazyBytes.car_Rentel.repository.UserRepository;
import com.EazyBytes.car_Rentel.services.auth.AuthService;
import com.EazyBytes.car_Rentel.services.jwt.UserService;
import com.EazyBytes.car_Rentel.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    private ResponseEntity<?> signupCustomer(@RequestBody SignUpRequest signUpRequest){
        if (authService.hasCustomerWithPhoneNumber(signUpRequest.getPhoneNumber())){
            return new ResponseEntity<>("Customer already exist with this phone number",HttpStatus.NOT_ACCEPTABLE);
        }
        if (authService.hasCustomerWithEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Customer already exist with this email",HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdCustomerDto =authService.createCustomer(signUpRequest);
        if (createdCustomerDto == null)return new ResponseEntity<>("Customer not created", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(createdCustomerDto,HttpStatus.CREATED);
    }

   @PostMapping("/login")
   public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
           BadCredentialsException, DisabledException, UsernameNotFoundException{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
       Optional<User> optionalUser = userRepository.findPersonByEmail(userDetails.getUsername());
       final String jwt = jwtUtil.generateToken(userDetails);
       AuthenticationResponse authenticationResponse = new AuthenticationResponse();
       if (optionalUser.isPresent()){
           authenticationResponse.setJwt(jwt);
           authenticationResponse.setUserID(optionalUser.get().getId());
           authenticationResponse.setUserRole(optionalUser.get().getUserRole());
       }
       return authenticationResponse;
   }
}