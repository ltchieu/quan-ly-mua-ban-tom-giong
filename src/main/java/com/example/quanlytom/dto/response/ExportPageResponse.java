package com.example.quanlytom.dto.response;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Value
public class ExportPageResponse implements Serializable {
    List<ExportResponse> exports;
    long totalItems;
    int currentPage;
    int totalPages;

    @Data
    public static class ExportResponse {
        private Integer exportId;
        private String exportDate;
        private BigDecimal totalAmount;
        private String customerName;
        private String paymentMethod;
    }
}
