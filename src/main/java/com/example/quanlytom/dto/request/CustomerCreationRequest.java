package com.example.quanlytom.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerCreationRequest implements Serializable {
    String fullName;
    String phoneNumber;
    String address;
}
