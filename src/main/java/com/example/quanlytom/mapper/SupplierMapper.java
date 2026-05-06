package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.request.SupplierCreationRequest;
import com.example.quanlytom.dto.response.SupplierDetailResponse;
import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.dto.response.SupplierResponse;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.entity.Supplier;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    @Mapping(source = "fullName", target = "nameSupplier")
    SupplierNameResponse toSupplierNameResponse(Supplier supplier);

    @Mapping(source = "fullName", target = "nameSupplier")
    SupplierResponse toSupplierResponse(Supplier supplier);

    List<SupplierNameResponse> toSupplierNameResponseList(List<Supplier> suppliers);

    List<SupplierResponse> toSupplierResponseList(List<Supplier> suppliers);

    @Mapping(source = "id", target = "importId")
    SupplierDetailResponse.ImportFromSupplier toImportFromSupplier(Import anImport);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSupplierFromRequest(SupplierCreationRequest request, @MappingTarget Supplier supplier);

    Supplier toNewSupplier(SupplierCreationRequest supplier);

    List<SupplierDetailResponse.ImportFromSupplier> toImportFromSupplierList(List<Import> imports);
}
