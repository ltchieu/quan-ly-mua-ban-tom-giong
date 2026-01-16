package com.example.quanlytom.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.quanlytom.entity.Import}
 */
@Value
public class ImportResponse implements Serializable {
    Integer id;
    LocalDateTime importDate;
    String supplierName;
    Integer supplierId;

    BigDecimal totalAmount;

    String paymentStatus;
}