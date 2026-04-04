package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.ExportPageResponse;
import com.example.quanlytom.entity.Export;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExportMapper {
    @Mapping(target = "exportId", source = "id")
    @Mapping(target = "exportDate", source = "exportDate")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "totalAmount", source = "totalPayment")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    ExportPageResponse.ExportResponse toExportResponse(Export export);

    List<ExportPageResponse.ExportResponse> toExportResponseList(List<Export> export);
}
