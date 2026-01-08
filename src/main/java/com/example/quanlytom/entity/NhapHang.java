package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Nhap hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [Nhap hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class NhapHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Ngay nhap hang")
    private LocalDateTime ngayNhapHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Nha cung cap")
    private NhaCungCap nhaCungCap;

    @Column(name = "Tong tien hang")
    private Double tongTienHang;

    @Column(name = "Trang thai thanh toan")
    private String trangThaiThanhToan;

    @Column(name = "Ghi chu", columnDefinition = "TEXT")
    private String ghiChu;

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

    @OneToMany(mappedBy = "nhapHang")
    @Builder.Default
    private List<CtNhapHang> chiTietNhapHangs = new ArrayList<>();
}
