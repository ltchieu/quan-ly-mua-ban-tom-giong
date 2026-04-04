package com.example.quanlytom.service;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
}
