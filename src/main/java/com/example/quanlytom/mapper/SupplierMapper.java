package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    @Mapping(source = "fullName", target = "nameSupplier")
    SupplierNameResponse toSupplierNameResponse(Supplier supplier);

    List<SupplierNameResponse> toSupplierNameResponseList(List<Supplier> suppliers);
}

