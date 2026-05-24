package com.example.quanlytom.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {
    private KpiResponse kpiCards;
    private List<RevenueByTimeResponse> revenueByTime;
    private List<TopProductResponse> topProducts;
    private List<BatchStatusResponse> batchStatusDistribution;
    private List<TopCustomerResponse> topCustomers;
    private List<TopSupplierResponse> topSuppliers;
    private List<LowStockWarningResponse> lowStockWarnings;
    private PaymentStatusOverviewResponse paymentStatus;
}
