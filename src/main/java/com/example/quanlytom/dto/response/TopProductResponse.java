package com.example.quanlytom.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopProductResponse {
    private String shrimpName;
    private String characteristic;
    private Double totalExportQuantity;
    private Double totalRevenue;
    private Double revenuePercentage;

    public interface Projection {
        String getShrimpName();
        String getCharacteristic();
        Double getTotalExportQuantity();
        Double getTotalRevenue();
        Double getRevenuePercentage();
    }
}
