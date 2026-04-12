package com.example.quanlytom.dto.request;

import lombok.Data;
import lombok.Value;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Value
public class ExportCreationRequest implements Serializable {
    String paymentMethod;
    Double totalPayment;
    Integer customerId;
    List<ExportDetailCreationRequest> exportDetails;

    @Data
    @Value
    public static class ExportDetailCreationRequest{
        Integer importDetailId;
        Double actualQuantity;
        Double returnedQuantity;
        BigDecimal subTotal;
        String returnReason;
        Integer batchId;
    }
}
