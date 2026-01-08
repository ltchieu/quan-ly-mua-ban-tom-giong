package com.example.quanlytom.service;

import com.example.quanlytom.entity.RefreshToken;
import com.example.quanlytom.entity.Users;
import com.example.quanlytom.exception.AppException;
import com.example.quanlytom.exception.ErrorCode;
import com.example.quanlytom.repository.RefreshTokenRepository;
import com.example.quanlytom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UsersRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken createRefreshToken(Integer userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        if (token.getRevoked())
            throw new AppException(ErrorCode.REFRESH_TOKEN_REVOKED);

        return token;
    }

    public RefreshToken rotateToken(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // Tạo token mới
        return createRefreshToken(oldToken.getUser().getId());
    }

    public void setRevoked(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
    }

    @Transactional
    public void deleteByUserId(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        refreshTokenRepository.deleteAllByUser(user);
    }

    public ResponseCookie createRefreshTokenCookie(String tokenValue, long maxAgeSeconds) {
        boolean isProduction = "prod".equalsIgnoreCase(activeProfile)
                || "production".equalsIgnoreCase(activeProfile);

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie
                .from("refreshToken", tokenValue)
                .httpOnly(true)
                .path("/auth/refreshtoken")
                .maxAge(Duration.ofSeconds(maxAgeSeconds));

        if (isProduction) {
            cookieBuilder.secure(true).sameSite("None");
        } else {
            cookieBuilder.secure(false);
        }

        return cookieBuilder.build();
    }

    public ResponseCookie createDeleteRefreshTokenCookie() {
        return createRefreshTokenCookie("", 0);
    }

}