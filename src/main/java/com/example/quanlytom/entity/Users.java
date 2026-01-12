package com.example.quanlytom.entity;

import com.example.quanlytom.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_phone", columnNames = "Phone"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "Email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE Users SET IsDeleted = 1, DeletedAt = GETDATE() WHERE id = ?")
@SQLRestriction("IsDeleted = 0")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Phone", length = 20, nullable = false)
    private String phone;

    @Email
    @Column(name = "Email", length = 255, nullable = false)
    private String email;

    @Column(name = "PasswordHash", length = 255, nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", length = 50, nullable = false)
    private Role role;

    // --- soft delete ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    @Column (name = "CreatedAt")
    private LocalDateTime createdAt;
}

