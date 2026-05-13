package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.BatchUpdateRequest;
import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.dto.response.BatchPageResponse;
import com.example.quanlytom.entity.Batch;
import com.example.quanlytom.enums.BatchStatus;
import com.example.quanlytom.mapper.BatchMapper;
import com.example.quanlytom.repository.BatchRepository;
import com.example.quanlytom.repository.ExportDetailRepository;
import com.example.quanlytom.repository.ImportDetailRepository;
import com.example.quanlytom.repository.ImportRepository;
import com.example.quanlytom.specification.GenericSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BatchService {
    final BatchRepository batchRepository;
    final BatchMapper batchMapper;
    final ImportRepository importRepository;
    final ImportDetailRepository importDetailRepository;
    final ExportDetailRepository exportDetailRepository;

    public List<BatchNameResponse> getAllBatchNames(){
        return batchMapper.toBatchNameResponseList(batchRepository.findAll());
    }

    public BatchPageResponse getAllBatch(
            int page, int size,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){
        Specification<Batch> batchSpecification = GenericSpecification.isBetweenDates(startDate,endDate, "createdDate");
        Pageable paging = PageRequest.of(page, size);

        Page<Batch> batchPage = batchRepository.findAll(batchSpecification, paging);
        List<BatchPageResponse.BatchResponse> batchList = batchMapper.toBatchReponseList(batchPage.getContent());

        return new BatchPageResponse(batchPage.getTotalElements(), batchPage.getNumber(), batchPage.getTotalPages(), batchList);
    }

    public BatchDetailResponse getDetailResponse(Integer batchId){
        Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"));
        BatchDetailResponse.ExportStatisticsProjection projection = exportDetailRepository.getExportStatsByBatch(batchId);

        BatchDetailResponse.ExportStatisticsDTO exportStatistics = new BatchDetailResponse.ExportStatisticsDTO(projection);
        exportStatistics.setCustomerList(exportDetailRepository.getCustomerListByBatch(batchId));

        BatchDetailResponse batchDetail = batchMapper.toBatchDetailResponse(batch);

        batchDetail.setShrimpList(importDetailRepository.getShrimpListInBatch(batchId));
        batchDetail.setImportReceipt(importRepository.getImportByBatch(batchId));
        batchDetail.setExportStatistics(exportStatistics);

        return batchDetail;
    }

    public BatchPageResponse.BatchResponse updateBatch(Integer batchId, BatchUpdateRequest batchUpdateRequest){
        Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"));
        batchMapper.toUpdateBatch(batchUpdateRequest, batch);
        batch = batchRepository.save(batch);
        return batchMapper.toBatchResponse(batch);
    }

    public void changBatchStatusToCanceled(Integer id){
        Batch batch = batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Batch not found"));
        batch.setStatus(BatchStatus.CANCELLED);

        batchRepository.save(batch);
    }
}
