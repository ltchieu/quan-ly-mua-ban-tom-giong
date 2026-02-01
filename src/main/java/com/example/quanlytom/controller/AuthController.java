package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.LoginRequest;
import com.example.quanlytom.dto.request.SignupRequest;
import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.LoginResponse;
import com.example.quanlytom.entity.RefreshToken;
import com.example.quanlytom.entity.Users;
import com.example.quanlytom.enums.Role;
import com.example.quanlytom.exception.AppException;
import com.example.quanlytom.exception.ErrorCode;
import com.example.quanlytom.security.jwt.JwtUtils;
import com.example.quanlytom.security.model.UserDetailsImpl;
import com.example.quanlytom.service.RefreshTokenService;
import com.example.quanlytom.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtToken(authentication);

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        // Set HTTP-only cookie for refresh token
        ResponseCookie cookie = refreshTokenService.createRefreshTokenCookie(
                refreshToken.getRefreshToken(),
                Duration.ofDays(7).getSeconds());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                refreshToken.getRefreshToken(),
                userDetails.getRole(),
                userDetails.getId()
        );

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .message("Login Successfully")
                        .data(loginResponse)
                        .build()
        );
    }

    // ===== SIGNUP =====
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        // Default role for new accounts
        Role role = Role.STAFF;

        userService.createUser(signupRequest, role, null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .message("Signup Successfully")
                        .build());
    }

    // ===== REFRESH TOKEN =====
    @PostMapping("/refreshtoken")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String cookieRefreshToken,
            @RequestBody(required = false) Map<String, String> body,
            HttpServletResponse response) {

        String requestRefreshToken = cookieRefreshToken;
        if (requestRefreshToken == null && body != null) {
            requestRefreshToken = body.get("refreshToken");
        }

        if (requestRefreshToken == null || requestRefreshToken.isBlank()) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenService.verifyExpiration(refreshToken);

        Users user = refreshToken.getUser();

        // revoke old refresh token
        refreshTokenService.setRevoked(refreshToken);

        // create new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

        String accessToken = jwtUtils.generateTokenFromIdentifier(user);

        // Set new refresh token cookie
        ResponseCookie cookie = refreshTokenService.createRefreshTokenCookie(
                newRefreshToken.getRefreshToken(),
                Duration.ofDays(7).getSeconds());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Map<String, String> tokenRefreshResponse = Map.of(
                "accessToken", accessToken,
                "refreshToken", newRefreshToken.getRefreshToken()
        );

        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .message("New Refresh Token and Access Token are created Successfully")
                .data(tokenRefreshResponse)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String identifier = authentication.getName();
            Users user = userService.getUserByIdentifier(identifier)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            refreshTokenService.deleteByUserId(user.getId());
            SecurityContextHolder.clearContext();

            ResponseCookie cookie = refreshTokenService.createDeleteRefreshTokenCookie();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return ResponseEntity.ok(ApiResponse.<Void>builder().message("Logout successful").build());
    }
}
