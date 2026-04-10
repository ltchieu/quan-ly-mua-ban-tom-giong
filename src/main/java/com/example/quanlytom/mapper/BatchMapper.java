package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.entity.Batch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchMapper {
    BatchNameResponse toBatchNameResponse(Batch batch);
    
    List<BatchNameResponse> toBatchNameResponseList(List<Batch> batches);
}

