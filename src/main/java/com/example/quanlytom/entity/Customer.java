package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "KhachHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [Khach hang] SET IsDeleted = 1, DeletedAt = GETDATE() WHERE id = ?")
@SQLRestriction("IsDeleted = 0")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "HoTen", columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(name = "Sdt", columnDefinition = "VARCHAR(255)")
    private String phoneNumber;

    @Column(name = "DiaChi", columnDefinition = "NVARCHAR(255)")
    private String address;

    // --- soft delete (UI only) ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<Export> exports = new ArrayList<>();
}
