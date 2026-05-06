package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.CustomerCreationRequest;
import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.CustomerDetailResponse;
import com.example.quanlytom.dto.response.CustomerPageResponse;
import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    final CustomerService customerService;

    @GetMapping()
    public ResponseEntity<ApiResponse<CustomerPageResponse>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        CustomerPageResponse customerPageResponse = customerService.getAllCustomers(name, page, size);
        return ResponseEntity.ok().body(ApiResponse.<CustomerPageResponse>builder().data(customerPageResponse).build());
    }

    @GetMapping("/customer-name")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomersName() {
        List<CustomerResponse> customers = customerService.getAllCustomersName();
        return ResponseEntity.ok().body(ApiResponse.<List<CustomerResponse>>builder().data(customers).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> getCustomerDetail(@PathVariable Integer id) {
        CustomerDetailResponse response = customerService.getCustomerDetail(id);
        return ResponseEntity.ok().body(ApiResponse.<CustomerDetailResponse>builder().data(response).build());
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<CustomerResponse>> createNewCustomer(@RequestBody CustomerCreationRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.ok().body(ApiResponse.<CustomerResponse>builder().data(response).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @RequestBody CustomerCreationRequest request,
            @PathVariable Integer id
    ) {
        CustomerResponse response = customerService.updateCustomer(request, id);
        return ResponseEntity.ok().body(ApiResponse.<CustomerResponse>builder().data(response).build());
    }
}
