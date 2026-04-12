package com.example.quanlytom.dto.response;

import lombok.Data;
import lombok.Value;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Value
public class ExportDetailResponse implements Serializable {
    ExportPageResponse.ExportResponse exportInfo;
    List<ExportDetailItem> exportDetails;

    @Data
    @Value
    public static class ExportDetailItem {
        Integer importDetailId;
        Double actualQuantity;
        Integer deductionRate;
        Integer returnedQuantity;
        String returnReason;
        BigDecimal totalAmount;
        BigDecimal unitPrice;
        ImportDetailResponse.ImportDetails importDetail;
    }
}
