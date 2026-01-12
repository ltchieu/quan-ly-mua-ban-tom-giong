package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CT nhap hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE [CT nhap hang] SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class ImportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "so luong nhap")
    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Tom")
    private Shrimp shrimp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma nhap hang")
    private Import importOrder;

    @Column(name = "Gia nhap")
    private Double importPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma lo hang")
    private Batch batch;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "importDetail")
    @Builder.Default
    private List<ExportDetail> exportDetails = new ArrayList<>();
}
