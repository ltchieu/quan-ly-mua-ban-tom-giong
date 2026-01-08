package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.SignupRequest;
import com.example.quanlytom.entity.Users;
import com.example.quanlytom.enums.Role;
import com.example.quanlytom.exception.AppException;
import com.example.quanlytom.exception.ErrorCode;
import com.example.quanlytom.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Users createUser(SignupRequest signupRequest, Role userType, String siteURL) {

        String phoneNumber = signupRequest.getPhoneNumber();

        if (userRepository.existsByPhone(phoneNumber) || userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_PHONE_OR_EMAIL_EXIST);
        }

        Users user = new Users();

        user.setEmail(signupRequest.getEmail());
        user.setPhone(phoneNumber);
        user.setRole(userType);
        user.setCreatedAt(LocalDateTime.now());
        user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
        return user;
    }

    public Optional<Users> getUserByIdentifier(String identifier) {
        return userRepository.findByPhoneOrEmail(identifier, identifier);
    }
}
