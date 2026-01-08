package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "CT xuat hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [CT xuat hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class CtXuatHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "So luong thuc giao")
    private Double soLuongThucGiao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma xuat hang")
    private XuatHang xuatHang;

    @Column(name = "So luong tra lai")
    private Double soLuongTraLai;

    @Column(name = "Don gia")
    private Double donGia;

    @Column(name = "Muc khau tru")
    private Integer mucKhauTru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma lo hang")
    private LoHang loHang;

    @Column(name = "Thanh tien")
    private Double thanhTien;

    @Column(name = "Ly do tra lai", columnDefinition = "TEXT")
    private String lyDoTraLai;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
