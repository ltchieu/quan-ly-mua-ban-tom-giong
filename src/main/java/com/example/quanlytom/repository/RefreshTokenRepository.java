package com.example.quanlytom.repository;

import com.example.quanlytom.entity.RefreshToken;
import com.example.quanlytom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
  Optional<RefreshToken> findByRefreshToken(String token);

  @Modifying
  int deleteByUser(Users user);

  @Modifying
  @Transactional
  void deleteAllByUser(Users user);
}