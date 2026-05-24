package com.example.quanlytom.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusOverviewResponse {
    private List<PaymentStatusItem> importPaymentStatus;
    private List<PaymentStatusItem> exportPaymentStatus;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentStatusItem {
        private String paymentStatus;
        private Long total;
        private Double totalAmount;
    }

    public interface PaymentStatusProjection {
        String getPaymentStatus();
        Long getTotal();
        Double getTotalAmount();
    }
}
