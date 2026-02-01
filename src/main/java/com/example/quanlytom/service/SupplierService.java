package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    final SupplierRepository supplierRepository;

    public List<SupplierNameResponse> getAllSupplierName() {
        return supplierRepository.findAll().stream().map(supplier -> {
            SupplierNameResponse response = new SupplierNameResponse();
            response.setId(supplier.getId());
            response.setNameSupplier(supplier.getFullName());
            return response;
        }).toList();
    }
}
