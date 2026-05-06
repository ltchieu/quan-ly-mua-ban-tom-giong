package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.SupplierCreationRequest;
import com.example.quanlytom.dto.response.*;
import com.example.quanlytom.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public ResponseEntity<ApiResponse<SupplierPageResponse>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false, defaultValue = "") String supplierName
    ) {
        SupplierPageResponse supplierPageResponse = supplierService.getAllSupplier(page, size, supplierName);
        return ResponseEntity.ok().body(ApiResponse.<SupplierPageResponse>builder().data(supplierPageResponse).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDetailResponse>> getDetailSupplier(@PathVariable Integer id){
        SupplierDetailResponse supplier = supplierService.getDetailSupplier(id);
        return ResponseEntity.ok().body(ApiResponse.<SupplierDetailResponse>builder().data(supplier).build());
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @RequestBody SupplierCreationRequest supplierCreationRequest
    ){
        SupplierResponse response = supplierService.createSupplier(supplierCreationRequest);
        return ResponseEntity.ok().body(ApiResponse.<SupplierResponse>builder().data(response).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable Integer id,
            @RequestBody SupplierCreationRequest supplierCreationRequest
    ){
        SupplierResponse response = supplierService.updateSupplier(supplierCreationRequest, id);
        return ResponseEntity.ok().body(ApiResponse.<SupplierResponse>builder().data(response).build());
    }
}
