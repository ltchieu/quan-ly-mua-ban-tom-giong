package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.CustomerCreationRequest;
import com.example.quanlytom.dto.response.CustomerDetailResponse;
import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.entity.Customer;
import com.example.quanlytom.entity.Export;
import com.example.quanlytom.mapper.CustomerMapper;
import com.example.quanlytom.repository.CustomerRepository;
import com.example.quanlytom.repository.ExportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    final CustomerRepository customerRepository;
    final CustomerMapper customerMapper;
    final ExportRepository exportRepository;

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toCustomerResponseList(customers);
    }

    public CustomerDetailResponse getCustomerDetail(Integer customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Export> exportList = exportRepository.findByCustomer_Id(customerId);

        CustomerDetailResponse customerDetailResponse = customerMapper.toCustomerDetailResponse(customer);
        List<CustomerDetailResponse.ExportsOfCustomer> exportsOfCustomer = customerMapper.toExportsOfCustomerList(exportList);
        customerDetailResponse.setExports(exportsOfCustomer);

        return customerDetailResponse;
    }

    public CustomerResponse createCustomer(CustomerCreationRequest customerCreationRequest){
        Customer newCustomer = customerMapper.toNewCustomer(customerCreationRequest);
        newCustomer.setDeleted(false);

        customerRepository.save(newCustomer);
        return customerMapper.toCustomerResponse(newCustomer);
    }

    public CustomerResponse updateCustomer(CustomerCreationRequest customerUpdateRequest, Integer customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        customerMapper.updateCustomerFromRequest(customerUpdateRequest, customer);
        customerRepository.save(customer);

        return customerMapper.toCustomerResponse(customer);
    }
}
