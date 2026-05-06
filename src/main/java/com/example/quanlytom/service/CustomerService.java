package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.CustomerCreationRequest;
import com.example.quanlytom.dto.response.CustomerDetailResponse;
import com.example.quanlytom.dto.response.CustomerPageResponse;
import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.entity.Customer;
import com.example.quanlytom.entity.Export;
import com.example.quanlytom.mapper.CustomerMapper;
import com.example.quanlytom.repository.CustomerRepository;
import com.example.quanlytom.repository.ExportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    final CustomerRepository customerRepository;
    final CustomerMapper customerMapper;
    final ExportRepository exportRepository;

    public List<CustomerResponse> getAllCustomersName() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toCustomerResponseList(customers);
    }

    public CustomerPageResponse getAllCustomers(
            String name,
            int page, int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<Customer> customerPage;

        customerPage = customerRepository.findAllByFullName(paging, name);

        List<CustomerResponse> customerResponses = customerMapper.toCustomerResponseList(customerPage.getContent());

        return new CustomerPageResponse(customerPage.getTotalElements(), customerPage.getNumber(), customerPage.getTotalPages(), customerResponses);
    }

    public CustomerDetailResponse getCustomerDetail(Integer customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Export> exportList = exportRepository.findByCustomer_Id(customerId);

        CustomerDetailResponse customerDetailResponse = customerMapper.toCustomerDetailResponse(customer);
        List<CustomerDetailResponse.ExportsOfCustomer> exportsOfCustomer = customerMapper.toExportsOfCustomerList(exportList);
        customerDetailResponse.setExports(exportsOfCustomer);

        return customerDetailResponse;
    }

    public CustomerResponse createCustomer(CustomerCreationRequest customerCreationRequest) {
        Customer newCustomer = customerMapper.toNewCustomer(customerCreationRequest);
        newCustomer.setDeleted(false);

        customerRepository.save(newCustomer);
        return customerMapper.toCustomerResponse(newCustomer);
    }

    public CustomerResponse updateCustomer(CustomerCreationRequest customerUpdateRequest, Integer customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        customerMapper.updateCustomerFromRequest(customerUpdateRequest, customer);
        customerRepository.save(customer);

        return customerMapper.toCustomerResponse(customer);
    }
}
