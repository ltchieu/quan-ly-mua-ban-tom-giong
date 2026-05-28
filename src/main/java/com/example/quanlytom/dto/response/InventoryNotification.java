package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryNotification {
    private Integer inventoryId;
    private String shrimpName;
    private String attributeName;
    private String batchName;
    private Double stockQuantity;
    private LocalDateTime storedAt;
    private Long hoursInInventory;
    private String alertType; // "WARNING" or "CRITICAL"
    private String message;
}
