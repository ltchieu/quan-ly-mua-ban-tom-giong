package com.example.quanlytom.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportCreationRequest implements Serializable {
    Double totalAmount;
    String paymentStatus;
    String note;
    Integer supplierId;
    Integer employeeId;
    List<ImportDetailRequest> importDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportDetailRequest{
        Integer id;
        Double quantity;
        BigDecimal price;
        Integer shrimpId;
        Integer attributeId;
    }
}
