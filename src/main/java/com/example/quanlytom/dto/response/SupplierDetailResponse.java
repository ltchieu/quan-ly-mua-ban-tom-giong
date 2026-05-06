package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Value
public class SupplierDetailResponse implements Serializable {
    SupplierResponse supplier;
    List<ImportFromSupplier> importFromSuppliers;

    @Data
    @AllArgsConstructor
    public static class ImportFromSupplier{
        Integer importId;
        LocalDateTime importDate;
        String paymentStatus;
        Double totalAmount;
        String note;
    }
}
