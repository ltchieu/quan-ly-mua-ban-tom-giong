package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.PaymentStatusOverviewResponse;
import com.example.quanlytom.dto.response.RevenueByTimeResponse;
import com.example.quanlytom.dto.response.TopCustomerResponse;
import com.example.quanlytom.entity.Export;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExportRepository extends JpaRepository<Export, Integer> {
    @EntityGraph(attributePaths = {"customer"})
    Page<Export> findAll(Specification<Export> specification, Pageable paging);

    List<Export> findByCustomer_Id(Integer customerId);

    // --- Statistics queries ---

    @Query(value = "SELECT COALESCE(SUM(tong_thanh_toan), 0) FROM xuat_hang WHERE is_deleted = 0", nativeQuery = true)
    Double sumTotalRevenue();

    @Query(value = """
        SELECT FORMAT(ngay_xuat, 'yyyy-MM') AS period,
               COALESCE(SUM(tong_thanh_toan), 0) AS totalAmount
        FROM xuat_hang
        WHERE is_deleted = 0
        GROUP BY FORMAT(ngay_xuat, 'yyyy-MM')
        ORDER BY period
        """, nativeQuery = true)
    List<RevenueByTimeResponse.PeriodAmountProjection> getExportRevenueByMonth();

    @Query(value = """
        SELECT hinh_thuc_thanh_toan AS paymentStatus,
               COUNT(*) AS total,
               COALESCE(SUM(tong_thanh_toan), 0) AS totalAmount
        FROM xuat_hang
        WHERE is_deleted = 0
        GROUP BY hinh_thuc_thanh_toan
        """, nativeQuery = true)
    List<PaymentStatusOverviewResponse.PaymentStatusProjection> getExportPaymentStatusStats();

    @Query(value = """
        SELECT TOP(:limit)
            kh.ho_ten                AS customerName,
            kh.sdt                   AS phone,
            COUNT(DISTINCT xh.id)    AS totalOrders,
            COALESCE(SUM(xh.tong_thanh_toan), 0) AS totalSpent,
            MAX(xh.ngay_xuat)        AS latestPurchaseDate
        FROM khach_hang kh
        JOIN xuat_hang xh ON xh.khach_hang = kh.id
        WHERE kh.is_deleted = 0 AND xh.is_deleted = 0
        GROUP BY kh.ho_ten, kh.sdt
        ORDER BY totalSpent DESC
        """, nativeQuery = true)
    List<TopCustomerResponse.Projection> getTopCustomers(@Param("limit") int limit);
}