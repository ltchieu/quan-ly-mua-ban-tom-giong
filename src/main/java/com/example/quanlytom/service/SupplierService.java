package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.mapper.SupplierMapper;
import com.example.quanlytom.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    final SupplierRepository supplierRepository;
    final SupplierMapper supplierMapper;

    public List<SupplierNameResponse> getAllSupplierName() {
        return supplierMapper.toSupplierNameResponseList(supplierRepository.findAll());
    }
}
