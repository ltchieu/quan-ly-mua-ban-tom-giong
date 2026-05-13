package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.request.ImportCreationRequest;
import com.example.quanlytom.entity.ImportDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ImportDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "importPrice", source = "price")
    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "shrimpAttribute", ignore = true)

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "importOrder", ignore = true)
    @Mapping(target = "exportDetails", ignore = true)
    ImportDetail toImportDetail(ImportCreationRequest.ImportDetailRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "importPrice", source = "price")
    @Mapping(target = "importOrder", ignore = true)
    @Mapping(target = "shrimpAttribute", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "exportDetails", ignore = true)
    void updateImportDetailFromRequest(ImportCreationRequest.ImportDetailRequest request, @MappingTarget ImportDetail importDetail);
}
