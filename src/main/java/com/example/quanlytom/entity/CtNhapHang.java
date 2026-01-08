package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "CT nhap hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [CT nhap hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class CtNhapHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "so luong nhap")
    private Double soLuongNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Tom")
    private Tom tom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma nhap hang")
    private NhapHang nhapHang;

    @Column(name = "Gia nhap")
    private Double giaNhap;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "ctNhapHang")
    private LoHang loHang;
}
