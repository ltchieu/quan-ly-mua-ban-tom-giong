package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "CTXuatHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [CTXuatHang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class ExportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SoLuongThucGiao")
    private Double actualQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaXuatHang")
    private Export export;

    @Column(name = "SoLuongTraLai")
    private Double returnedQuantity;

    @Column(name = "DonGia")
    private Double unitPrice;

    @Column(name = "MucKhauTru")
    private Integer deductionRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCtNhapHang")
    private ImportDetail importDetail;

    @Column(name = "ThanhTien")
    private Double subTotal;

    @Column(name = "LyDoTraLai", columnDefinition = "NVARCHAR(MAX)")
    private String returnReason;

    // --- soft delete (UI only) ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
