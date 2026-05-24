package com.example.quanlytom.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryDetailResponse{
    Integer id;
    Integer shrimpId;
    Integer attributeId;
    String shrimpName;
    String attributeName;
    Double stockQuantity;
    Double importQuantity;
    Double exportedQuantity;
    LocalDateTime updatedAt;
    Double returnedQuantity;   // Số lượng tôm bị khách trả lại
    Double DeadOrLostQuantity; //số lươn tôm chết hoặc mất mát


    BatchPageResponse.BatchResponse batch;
    PriceInfo price;

    @Data
    public static class PriceInfo {
        BigDecimal importPrice;
        Double totalImportCost;
        BigDecimal estimatedValue;
    }

}
