package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.*;
import com.example.quanlytom.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * All-in-one overview — returns all 8 statistics sections
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getOverview(
            @RequestParam(defaultValue = "5") int topLimit,
            @RequestParam(defaultValue = "50") double stockThreshold
    ) {
        StatisticsResponse res = statisticsService.getOverviewStatistics(topLimit, stockThreshold);
        return ResponseEntity.ok(ApiResponse.<StatisticsResponse>builder().data(res).build());
    }

    /**
     * 1. KPI Cards only
     */
    @GetMapping("/kpi")
    public ResponseEntity<ApiResponse<KpiResponse>> getKpi() {
        KpiResponse res = statisticsService.getKpiCards();
        return ResponseEntity.ok(ApiResponse.<KpiResponse>builder().data(res).build());
    }

    /**
     * 2. Revenue over time
     */
    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<List<RevenueByTimeResponse>>> getRevenue() {
        List<RevenueByTimeResponse> res = statisticsService.getRevenueByTime();
        return ResponseEntity.ok(ApiResponse.<List<RevenueByTimeResponse>>builder().data(res).build());
    }

    /**
     * 3. Top products
     */
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<TopProductResponse> res = statisticsService.getTopProducts(limit);
        return ResponseEntity.ok(ApiResponse.<List<TopProductResponse>>builder().data(res).build());
    }

    /**
     * 4. Batch status distribution
     */
    @GetMapping("/batch-status")
    public ResponseEntity<ApiResponse<List<BatchStatusResponse>>> getBatchStatus() {
        List<BatchStatusResponse> res = statisticsService.getBatchStatusDistribution();
        return ResponseEntity.ok(ApiResponse.<List<BatchStatusResponse>>builder().data(res).build());
    }

    /**
     * 5. Top customers
     */
    @GetMapping("/top-customers")
    public ResponseEntity<ApiResponse<List<TopCustomerResponse>>> getTopCustomers(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<TopCustomerResponse> res = statisticsService.getTopCustomers(limit);
        return ResponseEntity.ok(ApiResponse.<List<TopCustomerResponse>>builder().data(res).build());
    }

    /**
     * 6. Top suppliers
     */
    @GetMapping("/top-suppliers")
    public ResponseEntity<ApiResponse<List<TopSupplierResponse>>> getTopSuppliers(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<TopSupplierResponse> res = statisticsService.getTopSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.<List<TopSupplierResponse>>builder().data(res).build());
    }

    /**
     * 7. Low stock warnings
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<LowStockWarningResponse>>> getLowStockWarnings(
            @RequestParam(defaultValue = "50") double threshold
    ) {
        List<LowStockWarningResponse> res = statisticsService.getLowStockWarnings(threshold);
        return ResponseEntity.ok(ApiResponse.<List<LowStockWarningResponse>>builder().data(res).build());
    }

    /**
     * 8. Payment status overview
     */
    @GetMapping("/payment-status")
    public ResponseEntity<ApiResponse<PaymentStatusOverviewResponse>> getPaymentStatus() {
        PaymentStatusOverviewResponse res = statisticsService.getPaymentStatusOverview();
        return ResponseEntity.ok(ApiResponse.<PaymentStatusOverviewResponse>builder().data(res).build());
    }
}
