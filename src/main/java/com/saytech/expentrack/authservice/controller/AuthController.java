package com.saytech.expentrack.authservice.controller;

import com.saytech.expentrack.authservice.dto.ResponseDTO;
import com.saytech.expentrack.authservice.dto.UserDTO;
import com.saytech.expentrack.authservice.security.JwtUtil;
import com.saytech.expentrack.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody UserDTO userDTO) {
        return authService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserDTO userDTO) {
        return authService.login(userDTO);
    }

}
