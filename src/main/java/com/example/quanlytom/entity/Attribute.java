package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "TinhChat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [Tinh chat] SET IsDeleted = 1, DeletedAt = GETDATE() WHERE id = ?")
@SQLRestriction("IsDeleted = 0")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TenTinhChat", columnDefinition = "NVARCHAR(255)")
    private String name;

    // --- soft delete (UI only) ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
