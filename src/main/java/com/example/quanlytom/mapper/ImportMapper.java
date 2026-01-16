package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.ImportDetailResponse;
import com.example.quanlytom.dto.response.ImportResponse;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.entity.ImportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImportMapper {

    @Mapping(target = "supplierName", source = "supplier.fullName")
    @Mapping(target = "supplierId", source = "supplier.id")
    ImportResponse toImportResponse(Import importEntity);

    @Mapping(target = "importId", source = "id")
    @Mapping(target = "supplierName", source = "supplier.fullName")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "importDetails", source = "importDetails")
    ImportDetailResponse toImportDetailResponse(Import importEntity);

    @Mapping(target = "batchName", source = "batch.batchName")
    @Mapping(target = "batchId", source = "batch.id")
    @Mapping(target = "shrimpName", source = "shrimpAttribute.shrimp.name")
    @Mapping(target = "shrimpId", source = "shrimpAttribute.shrimp.id")
    @Mapping(target = "attributeName", source = "shrimpAttribute.attribute.name")
    @Mapping(target = "attributeId", source = "shrimpAttribute.attribute.id")
    @Mapping(target = "price", source = "importPrice")
    ImportDetailResponse.ImportDetails toImportDetails(ImportDetail importDetail);

    List<ImportResponse> toImportResponseList(List<Import> imports);
}

