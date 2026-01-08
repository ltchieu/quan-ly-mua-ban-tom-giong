package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Xuat hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [Xuat hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class XuatHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Ngay xuat")
    private LocalDateTime ngayXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Khach hang")
    private KhachHang khachHang;

    @Column(name = "Tong thanh toan")
    private Double tongThanhToan;

    @Column(name = "Hinh thuc thanh toan")
    private String hinhThucThanhToan;

    // --- auditing ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Users createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "xuatHang")
    @Builder.Default
    private List<CtXuatHang> chiTietXuatHangs = new ArrayList<>();
}
