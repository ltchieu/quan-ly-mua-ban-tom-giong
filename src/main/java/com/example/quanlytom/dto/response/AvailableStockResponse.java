package com.example.quanlytom.dto.response;

import lombok.Data;

@Data
public class AvailableStockResponse {
    Integer importDetailId;
    String shrimpName;
    String attributeName;
    Double remainingQuantity;
    Double importPrice;

    public AvailableStockResponse(Integer importDetailId, String shrimpName, String attributeName, Double remainingQuantity, Double importPrice) {
        this.importDetailId = importDetailId;
        this.shrimpName = shrimpName;
        this.attributeName = attributeName;
        this.remainingQuantity = remainingQuantity;
        this.importPrice = importPrice;
    }
}
