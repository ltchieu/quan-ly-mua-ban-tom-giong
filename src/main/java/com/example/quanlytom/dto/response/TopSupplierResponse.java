package com.example.quanlytom.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopSupplierResponse {
    private String supplierName;
    private String phone;
    private Long totalImports;
    private Double totalCost;
    private LocalDateTime latestImportDate;

    public interface Projection {
        String getSupplierName();
        String getPhone();
        Long getTotalImports();
        Double getTotalCost();
        LocalDateTime getLatestImportDate();
    }
}
