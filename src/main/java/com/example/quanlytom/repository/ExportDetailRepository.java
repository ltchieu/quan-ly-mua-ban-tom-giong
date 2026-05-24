package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.dto.response.TopProductResponse;
import com.example.quanlytom.entity.ExportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExportDetailRepository extends JpaRepository<ExportDetail, Integer> {
    @Query(value = """
            SELECT
                COUNT(DISTINCT xh.id)           AS totalExportCount,
                SUM(ctx.so_luong_thuc_giao)     AS totalExportQuantity,
                SUM(ctx.thanh_tien)             AS totalExportRevenue
            FROM lo_hang lh
            JOIN ctnhap_hang ct   ON ct.ma_lo_hang       = lh.id
            JOIN ctxuat_hang ctx  ON ctx.ma_ct_nhap_hang = ct.id
            JOIN xuat_hang xh     ON ctx.ma_xuat_hang    = xh.id
            WHERE lh.id          = :batchId
            """, nativeQuery = true)
    BatchDetailResponse.ExportStatisticsProjection getExportStatsByBatch(@Param("batchId") Integer batchId);

    // Query riêng để lấy danh sách khách hàng
    @Query(value = """
            SELECT kh.ho_ten AS customerName
            FROM lo_hang lh
            JOIN ctnhap_hang ct   ON ct.ma_lo_hang       = lh.id
            JOIN ctxuat_hang ctx  ON ctx.ma_ct_nhap_hang = ct.id
            JOIN xuat_hang xh     ON ctx.ma_xuat_hang    = xh.id
            JOIN khach_hang kh    ON xh.khach_hang       = kh.id
            WHERE lh.id          = :batchId
            GROUP BY kh.ho_ten
            """, nativeQuery = true)
    List<String> getCustomerListByBatch(@Param("batchId") Integer batchId);

    @Query("""
            SELECT COALESCE(SUM(ed.actualQuantity), 0.0)
            FROM ExportDetail ed
            WHERE ed.importDetail.batch.id = :batchId AND ed.importDetail.shrimpAttribute.id = :shrimpAttribute AND ed.isDeleted = false
            """)
    Double sumExportedQuantity(@Param("batchId") Integer batchId, @Param("shrimpAttribute") Integer shrimpAttribute);

    @Query("""
            SELECT COALESCE(SUM(ed.returnedQuantity), 0.0)
            FROM ExportDetail ed
            WHERE ed.importDetail.batch.id = :batchId AND ed.importDetail.shrimpAttribute.id = :shrimpAttribute AND ed.isDeleted = false
            """)
    Double sumReturnedQuantity(@Param("batchId") Integer batchId, @Param("shrimpAttribute") Integer shrimpAttribute);

    // --- Statistics queries ---

    @Query(value = "SELECT COALESCE(SUM(so_luong_thuc_giao), 0) FROM ctxuat_hang WHERE is_deleted = 0", nativeQuery = true)
    Double sumTotalExportQuantity();

    @Query(value = """
        SELECT TOP(:limit)
            t.Ten                           AS shrimpName,
            tc.ten_tinh_chat                AS characteristic,
            COALESCE(SUM(ctx.so_luong_thuc_giao), 0) AS totalExportQuantity,
            COALESCE(SUM(ctx.thanh_tien), 0)         AS totalRevenue,
            ROUND(
                COALESCE(SUM(ctx.thanh_tien), 0) * 100.0 /
                NULLIF(SUM(SUM(ctx.thanh_tien)) OVER(), 0), 2
            )                               AS revenuePercentage
        FROM ctxuat_hang ctx
        JOIN ctnhap_hang ct    ON ctx.ma_ct_nhap_hang  = ct.id
        JOIN tinhchat_tom tct  ON ct.ma_tinh_chat_tom  = tct.id
        JOIN Tom t             ON tct.ma_tom            = t.id
        JOIN tinh_chat tc      ON tct.ma_tinh_chat      = tc.id
        WHERE ctx.is_deleted = 0 AND ct.is_deleted = 0
        GROUP BY t.Ten, tc.ten_tinh_chat
        ORDER BY totalRevenue DESC
        """, nativeQuery = true)
    List<TopProductResponse.Projection> getTopProducts(@Param("limit") int limit);

}