package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BatchDetailResponse implements Serializable {
    // 1. Basic information
    private Integer id;
    private String batchName;
    private LocalDateTime createdAt;
    private String status;

    // 2. Shrimp list in batch
    private List<ShrimpInBatchDTO> shrimpList;

    // 3. Import receipt information
    private List<ImportReceiptDTO> importReceipt;

    // 4. Export statistics
    private ExportStatisticsDTO exportStatistics;

    public interface ShrimpInBatchDTO {
        String getShrimpName();
        String getCharacteristic();
        Double getImportQuantity();
        Double getImportPrice();
        BigDecimal getTotalAmount();
        Double getExportedQuantity();
        Double getRemainingQuantity();
    }

    public interface ImportReceiptDTO {
        Integer getReceiptId();
        LocalDateTime getImportDate();
        Double getTotalCost();
        String getPaymentStatus();
        String getNote();
        String getSupplierName();
        String getSupplierPhone();
        String getSupplierAddress();
    }

    public interface ExportStatisticsProjection {
        Integer getTotalExportCount();
        Double getTotalExportQuantity();
        Double getTotalExportRevenue();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExportStatisticsDTO {
        private Integer totalExportCount;
        private Double totalExportQuantity;
        private Double totalExportRevenue;
        private List<String> customerList;

        public ExportStatisticsDTO(ExportStatisticsProjection projection) {
            if (projection != null) {
                this.totalExportCount = projection.getTotalExportCount();
                this.totalExportQuantity = projection.getTotalExportQuantity();
                this.totalExportRevenue = projection.getTotalExportRevenue();
            }
        }
    }
}
