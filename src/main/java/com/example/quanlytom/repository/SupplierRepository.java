package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.TopSupplierResponse;
import com.example.quanlytom.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query("""
                select new Supplier(s.id, s.fullName, s.phoneNumber, s.address)
                from Supplier s
                where s.fullName like concat('%', :name, '%')
            """)
    Page<Supplier> findAllByFullName(String name, Pageable pageable);

    // --- Statistics queries ---

    @Query(value = "SELECT COUNT(*) FROM nha_cung_cap WHERE is_deleted = 0", nativeQuery = true)
    Long countAllActive();

    @Query(value = """
        SELECT TOP(:limit)
            ncc.ho_ten               AS supplierName,
            ncc.sdt                  AS phone,
            COUNT(DISTINCT nh.id)    AS totalImports,
            COALESCE(SUM(nh.tong_tien_hang), 0) AS totalCost,
            MAX(nh.ngay_nhap_hang)   AS latestImportDate
        FROM nha_cung_cap ncc
        JOIN nhap_hang nh ON nh.nha_cung_cap = ncc.id
        WHERE ncc.is_deleted = 0 AND nh.is_deleted = 0
        GROUP BY ncc.ho_ten, ncc.sdt
        ORDER BY totalCost DESC
        """, nativeQuery = true)
    List<TopSupplierResponse.Projection> getTopSuppliers(@Param("limit") int limit);
}