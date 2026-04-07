package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.ExportCreationRequest;
import com.example.quanlytom.dto.response.ExportDetailResponse;
import com.example.quanlytom.dto.response.ExportPageResponse;
import com.example.quanlytom.entity.Export;
import com.example.quanlytom.entity.ExportDetail;
import com.example.quanlytom.mapper.ExportDetailMapper;
import com.example.quanlytom.mapper.ExportMapper;
import com.example.quanlytom.repository.ExportDetailRepository;
import com.example.quanlytom.repository.ExportRepository;
import com.example.quanlytom.specification.GenericSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    final ExportRepository exportRepository;
    final ExportDetailRepository exportDetailRepository;
    final ExportMapper exportMapper;
    final ExportDetailMapper exportDetailMapper;

    public ExportPageResponse getAllExports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer customerId,
            int page, int size
    ) {
        Specification<Export> specification =
                GenericSpecification.<Export>hasJoinAttribute("customer", customerId)
                        .and(GenericSpecification.isBetweenDates(startDate, endDate, "exportDate"));
        Pageable paging = PageRequest.of(page, size, Sort.by("exportDate").descending());
        Page<Export> pagedResult = exportRepository.findAll(specification, paging);

        List<ExportPageResponse.ExportResponse> exportResponses = exportMapper.toExportResponseList(pagedResult.getContent());

        return new ExportPageResponse(exportResponses, pagedResult.getTotalElements(), pagedResult.getNumber(), pagedResult.getTotalPages());
    }

    public ExportDetailResponse getDetailsExport(int id) {
        Export export = exportRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Export does not found")
        );

        ExportPageResponse.ExportResponse exportInfo = exportMapper.toExportResponse(export);

        List<ExportDetailResponse.ExportDetailItem> detailItems = export.getExportDetails()
                .stream()
                .map(exportDetailMapper::toExportDetailItem)
                .toList();
        return new ExportDetailResponse(exportInfo, detailItems);
    }

    @Transactional
    public ExportPageResponse.ExportResponse saveExport(ExportCreationRequest exportCreationRequest){
        Export newExport = exportMapper.toNewExport(exportCreationRequest);
        newExport.setExportDate(LocalDateTime.now());
        newExport.setCreatedAt(LocalDateTime.now());
        newExport.setDeleted(false);

        exportRepository.save(newExport);

        if(exportCreationRequest.getExportDetails() != null){
            for (var item : exportCreationRequest.getExportDetails()){
                ExportDetail exportDetail = exportDetailMapper.toExportDetail(item);
                exportDetail.setExport(newExport);
                exportDetailRepository.save(exportDetail);
            }
        }

        return exportMapper.toExportResponse(newExport);
    }

    @Transactional
    public ExportPageResponse.ExportResponse updateExport(ExportCreationRequest exportUpdateRequest, Integer exportId){
        Export anExport = exportRepository.findById(exportId).orElseThrow(() -> new RuntimeException("Export not found"));
        exportMapper.updateExportFromRequest(exportUpdateRequest, anExport);
        exportRepository.save(anExport);

        if(exportUpdateRequest.getExportDetails() != null){
            List<ExportDetail> currentDetails = new ArrayList<>(anExport.getExportDetails());

            for (var item : exportUpdateRequest.getExportDetails()){
                ExportDetail existingDetail = null;
                if (item.getBatchId() != null) {
                    existingDetail = currentDetails.stream()
                        .filter(d -> d.getImportDetail() != null && 
                                     d.getImportDetail().getBatch() != null && 
                                     item.getBatchId().equals(d.getImportDetail().getBatch().getId()))
                        .findFirst().orElse(null);
                }

                if (existingDetail != null) {
                    exportDetailMapper.updateExportDetailFromRequest(item, existingDetail);
                    exportDetailRepository.save(existingDetail);
                    currentDetails.remove(existingDetail);
                } else {
                    ExportDetail newDetail = exportDetailMapper.toExportDetail(item);
                    newDetail.setExport(anExport);
                    exportDetailRepository.save(newDetail);
                }
            }
            
            if (!currentDetails.isEmpty()) {
                exportDetailRepository.deleteAll(currentDetails);
            }
        }

        return exportMapper.toExportResponse(anExport);
    }

    @Transactional
    public void deleteExport(Integer exportId) {
        Export anExport = exportRepository.findById(exportId).orElseThrow(() -> new RuntimeException("Export not found"));
        anExport.setDeleted(true);
        anExport.setDeletedAt(LocalDateTime.now());
        exportRepository.save(anExport);
    }
}
