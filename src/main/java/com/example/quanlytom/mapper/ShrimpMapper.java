package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.ShrimpDetailResponse;
import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.entity.Shrimp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ShrimpMapper {
    @Mapping(source = "name", target = "nameShrimp")
    ShrimpResponse toShrimpResponse(Shrimp shrimp);
    
    List<ShrimpResponse> toShrimpResponseList(List<Shrimp> shrimps);

    @Mapping(source = "name", target = "shrimpName")
    @Mapping(target = "statistics", ignore = true)
    ShrimpDetailResponse toShrimpDetailResponseBase(Shrimp shrimp);

    default ShrimpDetailResponse toShrimpDetailResponse(Shrimp shrimp, ShrimpDetailResponse.ShrimpStatistics statistics) {
        ShrimpDetailResponse response = toShrimpDetailResponseBase(shrimp);
        response.setStatistics(statistics);
        return response;
    }
}
