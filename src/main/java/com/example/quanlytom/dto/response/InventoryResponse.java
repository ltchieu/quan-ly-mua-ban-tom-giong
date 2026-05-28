package com.example.quanlytom.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryResponse {
    Integer id;
    Double stockQuantity;
    LocalDateTime updatedAt;
    LocalDateTime storedAt;
    LocalDateTime lastCheckedAt;
    String shrimpName;
    String attributeName;
    String batchName;
}
