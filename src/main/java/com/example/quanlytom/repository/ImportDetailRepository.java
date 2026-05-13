package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.entity.ImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportDetailRepository extends JpaRepository<ImportDetail, Integer> {

    @Query("""
                select new com.example.quanlytom.dto.response.AvailableStockResponse(
                    ctn.id,
                    t.name,
                    tc.name,
                    cast((ctn.quantity - COALESCE(SUM(ctx.actualQuantity), 0.0d)) as double),
                    ctn.importPrice
                )
                from ImportDetail ctn
            
                left join ExportDetail ctx on ctn.id = ctx.importDetail.id AND ctx.isDeleted = false
                join ShrimpAttribute tct on ctn.shrimpAttribute.id = tct.id
                join Shrimp t on t.id = tct.shrimp.id
                join Attribute tc on tc.id = tct.attribute.id
            
                where ctn.batch.id = :batchId AND ctn.isDeleted = false
                Group by
                	ctn.id,
                	t.name,
                    tc.name,
                	ctn.importPrice,
                    ctn.quantity
            """)
    List<AvailableStockResponse> findAvailableStockByBatchId(@Param("batchId") Integer batchId);

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
}