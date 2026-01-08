package com.example.quanlytom.security.jwt;

import com.example.quanlytom.entity.Users;
import com.example.quanlytom.exception.AppException;
import com.example.quanlytom.exception.ErrorCode;
import com.example.quanlytom.security.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("userId", userPrincipal.getId())
                .claim("role", userPrincipal.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        // Use getBytes() for plain text secrets instead of Base64 decoding
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public String getIdentifierFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String generateTokenFromIdentifier(Users user) {
        return Jwts.builder()
                .setSubject((user.getPhone()))
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new AppException(ErrorCode.INVALID_JWT_TOKEN);

        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.EXPIRED_JWT_TOKEN);

        } catch (UnsupportedJwtException e) {
            throw new AppException(ErrorCode.UNSUPPORT_TOKEN);

        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.JWT_CLAIMS_EMPTY);
        }
    }
}