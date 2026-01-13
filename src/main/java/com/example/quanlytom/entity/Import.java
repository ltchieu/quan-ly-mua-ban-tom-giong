package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NhapHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [NhapHang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NgayNhapHang")
    private LocalDateTime importDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhaCungCap")
    private Supplier supplier;

    @Column(name = "TongTienHang")
    private Double totalAmount;

    @Column(name = "TrangThaiThanhToan", columnDefinition = "NVARCHAR(255)")
    private String paymentStatus;

    @Column(name = "GhiChu", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    // --- auditing ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    private Users createdBy;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // --- soft delete (UI only) ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "importOrder")
    @Builder.Default
    private List<ImportDetail> importDetails = new ArrayList<>();
}
