package com.example.quanlytom.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopCustomerResponse {
    private String customerName;
    private String phone;
    private Long totalOrders;
    private Double totalSpent;
    private LocalDateTime latestPurchaseDate;

    public interface Projection {
        String getCustomerName();
        String getPhone();
        Long getTotalOrders();
        Double getTotalSpent();
        LocalDateTime getLatestPurchaseDate();
    }
}
