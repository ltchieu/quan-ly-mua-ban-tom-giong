package com.example.quanlytom.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerDetailResponse implements Serializable {
    Integer id;
    String fullName;
    String phoneNumber;
    String address;
    List<ExportsOfCustomer> exports;

    @Data
    public static class ExportsOfCustomer {
        Integer exportId;
        LocalDateTime exportDate;
        String paymentMethod;
        BigDecimal totalPayment;
    }
}
