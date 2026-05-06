package com.example.quanlytom.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SupplierResponse implements Serializable {
    Integer id;
    String nameSupplier;
    String address;
    String phoneNumber;
}
