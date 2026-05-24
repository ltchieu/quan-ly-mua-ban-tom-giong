package com.example.quanlytom.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueByTimeResponse {
    private String period;
    private Double totalImport;
    private Double totalExport;
    private Double profit;

    public interface PeriodAmountProjection {
        String getPeriod();
        Double getTotalAmount();
    }
}
