package com.example.quanlytom.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ShrimpDetailResponse implements Serializable {
    Integer id;
    String shrimpName;
    ShrimpStatistics statistics;

    public interface ShrimpStatistics {
        Integer getTotalNumberImport();
        Double getTotalQuantityImport();
        BigDecimal getTotalCostImport();
        Double getAvgImportPrice();
        Double getLatestImportPrice();
        LocalDate getLatestImportDate();
    }
}
