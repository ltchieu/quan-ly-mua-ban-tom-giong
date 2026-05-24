package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.dto.response.LowStockWarningResponse;
import com.example.quanlytom.entity.ImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportDetailRepository extends JpaRepository<ImportDetail, Integer> {

    @Query(value = """
                SELECT
                    t.Ten                                       AS shrimpName,
                    tc.ten_tinh_chat                            AS characteristic,
                    CAST(ct.so_luong_nhap AS FLOAT)             AS importQuantity,
                    CAST(ct.gia_nhap AS FLOAT)                  AS importPrice,
                    CAST(ct.gia_nhap * ct.so_luong_nhap AS FLOAT) AS totalAmount,
                    COALESCE(SUM(CAST(ctx.so_luong_thuc_giao AS FLOAT)), 0.0) AS exportedQuantity,
                    CAST(ct.so_luong_nhap AS FLOAT) - COALESCE(SUM(CAST(ctx.so_luong_thuc_giao AS FLOAT)), 0.0) AS remainingQuantity
                FROM lo_hang lh
                JOIN ctnhap_hang ct    ON ct.ma_lo_hang        = lh.id
                JOIN tinhchat_tom tct  ON ct.ma_tinh_chat_tom  = tct.id
                JOIN Tom t             ON tct.ma_tom            = t.id
                JOIN tinh_chat tc      ON tct.ma_tinh_chat      = tc.id
                LEFT JOIN ctxuat_hang ctx ON ctx.ma_ct_nhap_hang = ct.id AND ctx.is_deleted = 0
                WHERE lh.id         = :batchId
                GROUP BY t.Ten, tc.ten_tinh_chat, ct.so_luong_nhap, ct.gia_nhap
            """, nativeQuery = true)
    List<BatchDetailResponse.ShrimpInBatchDTO> getShrimpListInBatch(@Param("batchId") Integer batchId);

    @Query("""
            SELECT COALESCE(SUM(id.quantity), 0.0)
            FROM ImportDetail id
            WHERE id.batch.id = :batchId AND id.shrimpAttribute.id = :shrimpAttributeId AND id.isDeleted = false
            """)
    Double sumImportQuantity(@Param("batchId") Integer batchId, @Param("shrimpAttributeId") Integer shrimpAttributeId);

    @Query("""
            SELECT COALESCE(MAX(id.importPrice), 0.0)
            FROM ImportDetail id
            WHERE id.batch.id = :batchId AND id.shrimpAttribute.id = :shrimpAttributeId AND id.isDeleted = false
            """)
    Double getLatestImportPrice(@Param("batchId") Integer batchId, @Param("shrimpAttributeId") Integer shrimpAttributeId);

    @Query("""
            SELECT COALESCE(SUM(id.quantity * id.importPrice), 0.0)
            FROM ImportDetail id
            WHERE id.batch.id = :batchId AND id.shrimpAttribute.id = :shrimpAttributeId AND id.isDeleted = false
            """)
    Double sumTotalImportCost(@Param("batchId") Integer batchId, @Param("shrimpAttributeId") Integer shrimpAttributeId);

    List<ImportDetail> findByBatch_IdAndShrimpAttribute_Id(Integer batchId, Integer shrimpAttributeId);

    // --- Statistics queries ---

    @Query(value = "SELECT COALESCE(SUM(so_luong_nhap), 0) FROM ctnhap_hang WHERE is_deleted = 0", nativeQuery = true)
    Double sumTotalImportQuantity();

    @Query(value = """
        SELECT
            t.Ten                    AS shrimpName,
            tc.ten_tinh_chat         AS characteristic,
            lh.ten_lo                AS batchName,
            ct.so_luong_nhap - COALESCE(SUM(ctx.so_luong_thuc_giao), 0) AS remainingQuantity
        FROM ctnhap_hang ct
        JOIN tinhchat_tom tct   ON ct.ma_tinh_chat_tom  = tct.id
        JOIN Tom t              ON tct.ma_tom            = t.id
        JOIN tinh_chat tc       ON tct.ma_tinh_chat      = tc.id
        JOIN lo_hang lh         ON ct.ma_lo_hang         = lh.id
        LEFT JOIN ctxuat_hang ctx ON ctx.ma_ct_nhap_hang = ct.id AND ctx.is_deleted = 0
        WHERE ct.is_deleted = 0 AND lh.is_deleted = 0
        GROUP BY t.Ten, tc.ten_tinh_chat, lh.ten_lo, ct.so_luong_nhap
        HAVING (ct.so_luong_nhap - COALESCE(SUM(ctx.so_luong_thuc_giao), 0)) < :threshold
        ORDER BY remainingQuantity ASC
        """, nativeQuery = true)
    List<LowStockWarningResponse.Projection> getLowStockWarnings(@Param("threshold") double threshold);
}