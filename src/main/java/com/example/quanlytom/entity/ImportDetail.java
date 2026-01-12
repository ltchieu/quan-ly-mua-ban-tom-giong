package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CTNhapHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [CT nhap hang] SET IsDeleted = 1, DeletedAt = GETDATE() WHERE id = ?")
@SQLRestriction("IsDeleted = 0")
public class ImportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SoLuongNhap")
    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTinhChatTom")
    private ShrimpAttribute shrimpAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhapHang")
    private Import importOrder;

    @Column(name = "GiaNhap")
    private Double importPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLoHang")
    private Batch batch;

    // --- soft delete (UI only) ---
    @Column(name = "IsDeleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "importDetail")
    @Builder.Default
    private List<ExportDetail> exportDetails = new ArrayList<>();
}
