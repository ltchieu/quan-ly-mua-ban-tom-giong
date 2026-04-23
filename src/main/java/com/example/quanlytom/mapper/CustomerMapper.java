package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.request.CustomerCreationRequest;
import com.example.quanlytom.dto.response.CustomerDetailResponse;
import com.example.quanlytom.dto.response.CustomerResponse;
import com.example.quanlytom.entity.Customer;
import com.example.quanlytom.entity.Export;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "customerId", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "address", source = "address")
    CustomerResponse toCustomerResponse(Customer customer);
    List<CustomerResponse> toCustomerResponseList(List<Customer> customer);

    @Mapping(target = "exports", source = "exports")
    CustomerDetailResponse toCustomerDetailResponse(Customer customer);
    List<CustomerDetailResponse.ExportsOfCustomer> toExportsOfCustomerList(List<Export> exports);

    @Mapping(target = "exportId", source = "id")
    @Mapping(target = "exportDate", source = "exportDate")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "totalPayment", source = "totalPayment")
    CustomerDetailResponse.ExportsOfCustomer toExportsOfCustomer(Export export);

    Customer toNewCustomer(CustomerCreationRequest newCustomer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromRequest(CustomerCreationRequest request, @MappingTarget Customer customer);
}
