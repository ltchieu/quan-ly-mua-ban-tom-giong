package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.entity.Customer;
import com.example.quanlytom.mapper.CustomerMapper;
import com.example.quanlytom.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    final CustomerRepository customerRepository;
    final CustomerMapper customerMapper;

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toCustomerResponseList(customers);
    }
}
