package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    final CustomerService customerService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok().body(ApiResponse.<List<CustomerResponse>>builder().data(customers).build());
    }
}
