package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.ExportDetailResponse;
import com.example.quanlytom.dto.request.ExportCreationRequest;
import com.example.quanlytom.entity.ExportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExportDetailMapper {

    @Mapping(target = "totalAmount", source = "subTotal")
    @Mapping(target = "importDetailId", source = "importDetail.id")
    @Mapping(target = "importDetail.price", source = "importDetail.importPrice")
    @Mapping(target = "importDetail.quantity", source = "importDetail.quantity")
    @Mapping(target = "importDetail.batchName", source = "importDetail.batch.batchName")
    @Mapping(target = "importDetail.batchId", source = "importDetail.batch.id")
    @Mapping(target = "importDetail.shrimpName", source = "importDetail.shrimpAttribute.shrimp.name")
    @Mapping(target = "importDetail.shrimpId", source = "importDetail.shrimpAttribute.shrimp.id")
    @Mapping(target = "importDetail.attributeName", source = "importDetail.shrimpAttribute.attribute.name")
    @Mapping(target = "importDetail.attributeId", source = "importDetail.shrimpAttribute.attribute.id")
    ExportDetailResponse.ExportDetailItem toExportDetailItem(ExportDetail exportDetail);

    @Mapping(target = "importDetail", ignore = true)
    ExportDetail toExportDetail(ExportCreationRequest.ExportDetailCreationRequest request);

    @Mapping(target = "importDetail", ignore = true)
    void updateExportDetailFromRequest(ExportCreationRequest.ExportDetailCreationRequest request, @MappingTarget ExportDetail exportDetail);
}
