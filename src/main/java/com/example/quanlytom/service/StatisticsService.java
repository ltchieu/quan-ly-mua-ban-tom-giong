package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.*;
import com.example.quanlytom.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;
    private final ImportDetailRepository importDetailRepository;
    private final ExportDetailRepository exportDetailRepository;
    private final BatchRepository batchRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    /**
     * 1. KPI Cards — Financial + Operational
     */
    public KpiResponse getKpiCards() {
        Double totalRevenue = exportRepository.sumTotalRevenue();
        Double totalCost = importRepository.sumTotalCost();
        Double totalImportQty = importDetailRepository.sumTotalImportQuantity();
        Double totalExportQty = exportDetailRepository.sumTotalExportQuantity();
        Long totalBatch = batchRepository.count();
        Long activeBatch = batchRepository.getBatchStatusDistribution().stream()
                .filter(b -> "IN_PROGRESS".equals(b.getStatus()))
                .map(BatchStatusResponse.Projection::getTotal)
                .findFirst()
                .orElse(0L);
        Long totalCustomer = customerRepository.countAllActive();
        Long totalSupplier = supplierRepository.countAllActive();

        return KpiResponse.builder()
                .totalRevenue(totalRevenue)
                .totalCost(totalCost)
                .estimatedProfit(totalRevenue - totalCost)
                .estimatedStock(totalImportQty - totalExportQty)
                .totalBatch(totalBatch)
                .activeBatch(activeBatch)
                .totalCustomer(totalCustomer)
                .totalSupplier(totalSupplier)
                .build();
    }

    /**
     * 2. Revenue over time — grouped by month
     */
    public List<RevenueByTimeResponse> getRevenueByTime() {
        List<RevenueByTimeResponse.PeriodAmountProjection> importData = importRepository.getImportRevenueByMonth();
        List<RevenueByTimeResponse.PeriodAmountProjection> exportData = exportRepository.getExportRevenueByMonth();

        // Build maps: period → amount
        Map<String, Double> importMap = new LinkedHashMap<>();
        for (RevenueByTimeResponse.PeriodAmountProjection p : importData) {
            importMap.put(p.getPeriod(), p.getTotalAmount());
        }

        Map<String, Double> exportMap = new LinkedHashMap<>();
        for (RevenueByTimeResponse.PeriodAmountProjection p : exportData) {
            exportMap.put(p.getPeriod(), p.getTotalAmount());
        }

        // Merge all periods
        Set<String> allPeriods = new TreeSet<>();
        allPeriods.addAll(importMap.keySet());
        allPeriods.addAll(exportMap.keySet());

        return allPeriods.stream().map(period -> {
            Double totalImport = importMap.getOrDefault(period, 0.0);
            Double totalExport = exportMap.getOrDefault(period, 0.0);
            return RevenueByTimeResponse.builder()
                    .period(period)
                    .totalImport(totalImport)
                    .totalExport(totalExport)
                    .profit(totalExport - totalImport)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 3. Top selling products
     */
    public List<TopProductResponse> getTopProducts(int limit) {
        return exportDetailRepository.getTopProducts(limit).stream()
                .map(p -> TopProductResponse.builder()
                        .shrimpName(p.getShrimpName())
                        .characteristic(p.getCharacteristic())
                        .totalExportQuantity(p.getTotalExportQuantity())
                        .totalRevenue(p.getTotalRevenue())
                        .revenuePercentage(p.getRevenuePercentage())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 4. Batch status distribution (donut chart)
     */
    public List<BatchStatusResponse> getBatchStatusDistribution() {
        return batchRepository.getBatchStatusDistribution().stream()
                .map(p -> BatchStatusResponse.builder()
                        .status(p.getStatus())
                        .total(p.getTotal())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 5. Top customers
     */
    public List<TopCustomerResponse> getTopCustomers(int limit) {
        return exportRepository.getTopCustomers(limit).stream()
                .map(p -> TopCustomerResponse.builder()
                        .customerName(p.getCustomerName())
                        .phone(p.getPhone())
                        .totalOrders(p.getTotalOrders())
                        .totalSpent(p.getTotalSpent())
                        .latestPurchaseDate(p.getLatestPurchaseDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 6. Top suppliers
     */
    public List<TopSupplierResponse> getTopSuppliers(int limit) {
        return supplierRepository.getTopSuppliers(limit).stream()
                .map(p -> TopSupplierResponse.builder()
                        .supplierName(p.getSupplierName())
                        .phone(p.getPhone())
                        .totalImports(p.getTotalImports())
                        .totalCost(p.getTotalCost())
                        .latestImportDate(p.getLatestImportDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 7. Low stock warnings
     */
    public List<LowStockWarningResponse> getLowStockWarnings(double threshold) {
        return importDetailRepository.getLowStockWarnings(threshold).stream()
                .map(p -> LowStockWarningResponse.builder()
                        .shrimpName(p.getShrimpName())
                        .characteristic(p.getCharacteristic())
                        .batchName(p.getBatchName())
                        .remainingQuantity(p.getRemainingQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 8. Payment status overview (2 donut charts)
     */
    public PaymentStatusOverviewResponse getPaymentStatusOverview() {
        List<PaymentStatusOverviewResponse.PaymentStatusItem> importStatus =
                importRepository.getImportPaymentStatusStats().stream()
                        .map(p -> PaymentStatusOverviewResponse.PaymentStatusItem.builder()
                                .paymentStatus(p.getPaymentStatus())
                                .total(p.getTotal())
                                .totalAmount(p.getTotalAmount())
                                .build())
                        .collect(Collectors.toList());

        List<PaymentStatusOverviewResponse.PaymentStatusItem> exportStatus =
                exportRepository.getExportPaymentStatusStats().stream()
                        .map(p -> PaymentStatusOverviewResponse.PaymentStatusItem.builder()
                                .paymentStatus(p.getPaymentStatus())
                                .total(p.getTotal())
                                .totalAmount(p.getTotalAmount())
                                .build())
                        .collect(Collectors.toList());

        return PaymentStatusOverviewResponse.builder()
                .importPaymentStatus(importStatus)
                .exportPaymentStatus(exportStatus)
                .build();
    }

    /**
     * All-in-one overview statistics
     */
    public StatisticsResponse getOverviewStatistics(int topLimit, double stockThreshold) {
        return StatisticsResponse.builder()
                .kpiCards(getKpiCards())
                .revenueByTime(getRevenueByTime())
                .topProducts(getTopProducts(topLimit))
                .batchStatusDistribution(getBatchStatusDistribution())
                .topCustomers(getTopCustomers(topLimit))
                .topSuppliers(getTopSuppliers(topLimit))
                .lowStockWarnings(getLowStockWarnings(stockThreshold))
                .paymentStatus(getPaymentStatusOverview())
                .build();
    }
}
