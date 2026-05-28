package com.example.quanlytom.dto.response;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class ImportDetailResponse implements Serializable {
    Integer importId;
    LocalDateTime importDate;
    String supplierName;
    Integer supplierId;
    BigDecimal totalAmount;
    String paymentStatus;
    String createBy;
    List<ImportDetails> importDetails;

    @Value
    @Setter
    @Getter
    public static class ImportDetails {
        Integer importDetailId;
        BigDecimal price;
        Double quantity;
        String batchName;
        Integer batchId;
        String shrimpName;
        Integer shrimpId;
        String attributeName;
        Integer attributeId;
    }
}
