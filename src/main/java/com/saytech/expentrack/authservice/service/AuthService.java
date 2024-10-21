package com.saytech.expentrack.authservice.service;

import com.saytech.expentrack.authservice.dto.ResponseDTO;
import com.saytech.expentrack.authservice.dto.UserDTO;
import com.saytech.expentrack.authservice.entity.User;
import com.saytech.expentrack.authservice.repository.UserRepository;
import com.saytech.expentrack.authservice.security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ResponseDTO> registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new ResponseDTO(userDTO.getUsername(), "Email already exists","" ));
        }
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(userDTO.getUsername(), "Username already exists","" ));
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(201).body(new ResponseDTO(userDTO.getUsername(), "User registered successfully","" ));
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public ResponseEntity<ResponseDTO> login (UserDTO userDTO) {
        String token = null;
        Optional<User> savedUser = getUserByEmail(userDTO.getEmail());
        if (savedUser.isPresent()) {
            if (passwordEncoder.matches(userDTO.getPassword(), savedUser.get().getPassword())) {
                token = jwtUtil.generateToken(userDTO.getUsername());
                return ResponseEntity.ok(new ResponseDTO(userDTO.getUsername(), "User log in successful",token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(userDTO.getUsername(), "Invalid credentials",""));
    }

}
