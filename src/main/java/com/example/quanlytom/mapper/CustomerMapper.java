package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "customerId", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "address", source = "address")
    CustomerResponse toCustomerResponse(Customer customer);
    List<CustomerResponse> toCustomerResponseList(List<Customer> customer);
}
