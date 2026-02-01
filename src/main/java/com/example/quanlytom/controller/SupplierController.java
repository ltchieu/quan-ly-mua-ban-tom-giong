package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@AllArgsConstructor
public class SupplierController {
    final SupplierService supplierService;

    @GetMapping("/names")
    public ResponseEntity<ApiResponse<List<SupplierNameResponse>>> getAllSuppliersName() {
        List<SupplierNameResponse> suppliers = supplierService.getAllSupplierName();
        return ResponseEntity.ok().body(ApiResponse.<List<SupplierNameResponse>>builder().data(suppliers).build());
    }
}
