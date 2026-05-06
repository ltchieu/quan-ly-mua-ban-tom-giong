package com.example.quanlytom.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierCreationRequest implements Serializable {
    String fullName;
    String phoneNumber;
    String address;
}
