package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.ShrimpDetailResponse;
import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.entity.Shrimp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShrimpRepository extends JpaRepository<Shrimp, Integer> {
    @Query(value = """
            SELECT
                COUNT(DISTINCT nh.id)                       AS totalNumberImport,
                SUM(ct.so_luong_nhap)                       AS totalQuantityImport,
                SUM(ct.gia_nhap * ct.so_luong_nhap)         AS totalCostImport,
                ROUND(AVG(ct.gia_nhap), 2)                  AS avgImportPrice,
                MAX(nh.ngay_nhap_hang)                      AS latestImportDate,
                (
                    SELECT ct2.gia_nhap
                    FROM ctnhap_hang ct2
                    JOIN tinhchat_tom tct2 ON ct2.ma_tinh_chat_tom = tct2.id
                    JOIN nhap_hang nh2     ON ct2.ma_nhap_hang = nh2.id
                    WHERE tct2.ma_tom = :shrimpId
                      AND ct2.is_deleted = 0
                      AND nh2.is_deleted = 0
                    ORDER BY nh2.ngay_nhap_hang DESC
                    OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY
                )                                           AS latestImportPrice
            FROM tom t
            JOIN tinhchat_tom tct   ON tct.ma_tom           = t.id
            JOIN ctnhap_hang ct     ON ct.ma_tinh_chat_tom  = tct.id
            JOIN nhap_hang nh       ON ct.ma_nhap_hang      = nh.id
            WHERE t.id          = :shrimpId
            """, nativeQuery = true)
    ShrimpDetailResponse.ShrimpStatistics getShrimpStatistics(@Param("shrimpId") Integer shrimpId);

    Page<Shrimp> findAllByNameContaining(String name, Pageable pageable);
}