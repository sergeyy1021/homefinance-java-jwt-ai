package com.sergey1021.homefinance.controller;

import com.sergey1021.homefinance.dto.LoginDto;
import com.sergey1021.homefinance.entity.Users;
import com.sergey1021.homefinance.repository.UserRepository;
import com.sergey1021.homefinance.response.LoginResponse;
import com.sergey1021.homefinance.service.JWTService;
import com.sergey1021.homefinance.service.MyUserDetailsService;
import com.sergey1021.homefinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto LoginDto) {

        Users user = (userService.login(LoginDto));

        String jwtToken = jwtService.generateToken(user.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setTokenExpireTime(jwtService.getTokenExpireTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    @CrossOrigin("*")
    public ResponseEntity<?> register(@RequestBody Users user) {
        String registrationStatus = myUserDetailsService.registerUser(user);
        if (registrationStatus.equals("User already exists")) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }
        return ResponseEntity.ok(userService.register(user));
    }
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", emailExists);
        return ResponseEntity.ok(response);
    }

}

