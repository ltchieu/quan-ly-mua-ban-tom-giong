package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "TonKho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE ton_kho SET is_deleted = 1, deleted_at = GETDATE() WHERE id = ?")
@SQLRestriction("is_deleted = 0")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTinhChatTom", nullable = false)
    private ShrimpAttribute shrimpAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLoHang", nullable = false)
    private Batch batch;

    @Column(name = "SoLuongTon", nullable = false)
    @Builder.Default
    private Double stockQuantity = 0.0;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "IsDeleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}

