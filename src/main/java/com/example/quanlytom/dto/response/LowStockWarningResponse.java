package com.example.quanlytom.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LowStockWarningResponse {
    private String shrimpName;
    private String characteristic;
    private String batchName;
    private Double remainingQuantity;

    public interface Projection {
        String getShrimpName();
        String getCharacteristic();
        String getBatchName();
        Double getRemainingQuantity();
    }
}
