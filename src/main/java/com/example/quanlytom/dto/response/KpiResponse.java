package com.example.quanlytom.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiResponse {
    // Row 1: Financial
    private Double totalRevenue;
    private Double totalCost;
    private Double estimatedProfit;
    private Double estimatedStock;

    // Row 2: Operational
    private Long totalBatch;
    private Long activeBatch;
    private Long totalCustomer;
    private Long totalSupplier;
}
