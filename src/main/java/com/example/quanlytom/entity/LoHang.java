package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Lo hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [Lo hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class LoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma nhap hang")
    private CtNhapHang ctNhapHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Tom")
    private Tom tom;

    @Column(name = "Tong so luong nhap")
    private Double tongSoLuongNhap;

    @Column(name = "Trang thai")
    private String trangThai;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "loHang")
    @Builder.Default
    private List<CtXuatHang> chiTietXuatHangs = new ArrayList<>();
}
