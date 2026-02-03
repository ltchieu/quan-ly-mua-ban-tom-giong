package com.example.quanlytom.dto.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Integer customerId;
    private String fullName;
    private String phoneNumber;
    private String address;
}
