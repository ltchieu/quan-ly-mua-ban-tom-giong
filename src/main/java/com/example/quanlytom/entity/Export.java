package com.example.quanlytom.entity;

import com.example.quanlytom.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XuatHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [XuatHang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Export {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NgayXuat")
    private LocalDateTime exportDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KhachHang")
    private Customer customer;

    @Column(name = "TongThanhToan")
    private Double totalPayment;

    @Column(name = "HinhThucThanhToan", columnDefinition = "NVARCHAR(255)")
    private String paymentMethod;

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

    @OneToMany(mappedBy = "export")
    @Builder.Default
    private List<ExportDetail> exportDetails = new ArrayList<>();
}
