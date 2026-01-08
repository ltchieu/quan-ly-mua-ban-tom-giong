package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);

    Optional<Users> findByPhoneOrEmail(String phone, String email);

    Optional<Users> findById(Integer id);

    Optional<Users> findByPhone(String phone);

    Optional<Users> findByEmail(String email);
}
