package com.example.quanlytom.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableStockResponse {
    Integer importDetailId;
    String shrimpName;
    String attributeName;
    Double remainingQuantity;
    Double importPrice;

}
