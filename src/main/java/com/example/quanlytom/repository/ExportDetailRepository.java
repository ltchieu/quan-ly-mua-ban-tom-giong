package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.BatchDetailResponse;
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

}