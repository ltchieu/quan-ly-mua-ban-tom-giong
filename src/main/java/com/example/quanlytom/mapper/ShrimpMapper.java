package com.example.quanlytom.mapper;

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
}
