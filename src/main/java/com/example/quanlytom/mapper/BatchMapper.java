package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.request.BatchUpdateRequest;
import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.dto.response.BatchPageResponse;
import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.entity.Batch;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BatchMapper {
    BatchNameResponse toBatchNameResponse(Batch batch);

    BatchPageResponse.BatchResponse toBatchResponse(Batch batch);

    List<BatchPageResponse.BatchResponse> toBatchReponseList(List<Batch> batches);
    
    List<BatchNameResponse> toBatchNameResponseList(List<Batch> batches);

    @Mapping(source = "createdDate", target = "createdAt")
    @Mapping(target = "shrimpList", ignore = true)
    @Mapping(target = "importReceipt", ignore = true)
    @Mapping(target = "exportStatistics", ignore = true)
    BatchDetailResponse toBatchDetailResponse(Batch batch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateBatch(BatchUpdateRequest batchUpdateRequest, @MappingTarget Batch batch);
}
