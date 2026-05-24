package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.dto.response.PaymentStatusOverviewResponse;
import com.example.quanlytom.dto.response.RevenueByTimeResponse;
import com.example.quanlytom.entity.Import;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImportRepository extends JpaRepository<Import, Integer>, JpaSpecificationExecutor<Import> {

    @Override
    @EntityGraph(attributePaths = {"supplier"})
    Page<Import> findAll(Specification<Import> spec, Pageable pageable);

    @EntityGraph(attributePaths = {
            "supplier",
            "importDetails",
            "importDetails.batch",
            "importDetails.shrimpAttribute",
            "importDetails.shrimpAttribute.shrimp",
            "importDetails.shrimpAttribute.attribute"
    })
    @Query("SELECT i FROM Import i WHERE i.id = :id")
    Optional<Import> findWithDetailsById(@Param("id") Integer id);

    List<Import> findBySupplier_Id(Integer supplierId);

    @Query(value = """
        SELECT
            nh.id                           AS receiptId,
            nh.ngay_nhap_hang               AS importDate,
            nh.tong_tien_hang               AS totalCost,
            nh.trang_thai_thanh_toan        AS paymentStatus,
            nh.ghi_chu                      AS note,
            ncc.ho_ten                      AS supplierName,
            ncc.sdt                         AS supplierPhone,
            ncc.dia_chi                     AS supplierAddress
        FROM lo_hang lh
        JOIN ctnhap_hang ct   ON ct.ma_lo_hang   = lh.id
        JOIN nhap_hang nh     ON ct.ma_nhap_hang = nh.id
        JOIN nha_cung_cap ncc ON nh.nha_cung_cap = ncc.id
        WHERE lh.id         = :batchId
        GROUP BY nh.id, nh.ngay_nhap_hang, nh.tong_tien_hang,
                 nh.trang_thai_thanh_toan, nh.ghi_chu,
                 ncc.ho_ten, ncc.sdt, ncc.dia_chi
        """, nativeQuery = true)
   List<BatchDetailResponse.ImportReceiptDTO> getImportByBatch(@Param("batchId") Integer batchId);

    // --- Statistics queries ---

    @Query(value = "SELECT COALESCE(SUM(tong_tien_hang), 0) FROM nhap_hang WHERE is_deleted = 0", nativeQuery = true)
    Double sumTotalCost();

    @Query(value = """
        SELECT FORMAT(ngay_nhap_hang, 'yyyy-MM') AS period,
               COALESCE(SUM(tong_tien_hang), 0) AS totalAmount
        FROM nhap_hang
        WHERE is_deleted = 0
        GROUP BY FORMAT(ngay_nhap_hang, 'yyyy-MM')
        ORDER BY period
        """, nativeQuery = true)
    List<RevenueByTimeResponse.PeriodAmountProjection> getImportRevenueByMonth();

    @Query(value = """
        SELECT trang_thai_thanh_toan AS paymentStatus,
               COUNT(*) AS total,
               COALESCE(SUM(tong_tien_hang), 0) AS totalAmount
        FROM nhap_hang
        WHERE is_deleted = 0
        GROUP BY trang_thai_thanh_toan
        """, nativeQuery = true)
    List<PaymentStatusOverviewResponse.PaymentStatusProjection> getImportPaymentStatusStats();
}