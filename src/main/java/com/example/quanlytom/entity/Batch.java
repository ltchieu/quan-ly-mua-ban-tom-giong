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
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Trang thai", columnDefinition = "NVARCHAR(255)")
    private String status;

    @Column(name = "Ten lo", columnDefinition = "NVARCHAR(255)")
    private String batchName;

    @Column(name = "Ngay tao")
    private LocalDateTime createdDate;

    // --- soft delete (UI only) ---
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "batch")
    @Builder.Default
    private List<ImportDetail> importDetails = new ArrayList<>();
}
