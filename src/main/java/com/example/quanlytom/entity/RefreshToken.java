package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "RefreshToken",nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "Hethan", nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Manguoidung", nullable = false)
    private Users user;

    @Column(name = "Dathuhoi", nullable = false)
    private Boolean revoked = false;
}
