package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.ImportDetailResponse;
import com.example.quanlytom.dto.response.ImportPageResponse;
import com.example.quanlytom.dto.response.ImportResponse;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.repository.ImportRepository;
import com.example.quanlytom.specification.ImportSpec;
import com.example.quanlytom.mapper.ImportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    public final ImportRepository importRepository;
    private final ImportMapper importMapper;

    public ImportPageResponse getAllImports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer supplierId,
            int page, int size
    ) {
        Specification<Import> specification = Specification
                .where(ImportSpec.hasSupplier(supplierId))
                .and(ImportSpec.isBetweenDates(startDate, endDate));

        Pageable paging = PageRequest.of(page, size, Sort.by("importDate").descending());
        Page<Import> pagedResult = importRepository.findAll(specification, paging);

        List<ImportResponse> importResponses = importMapper.toImportResponseList(pagedResult.getContent());
        return new ImportPageResponse(importResponses, pagedResult.getTotalElements(), pagedResult.getNumber(), pagedResult.getTotalPages());
    }

    public ImportDetailResponse getImportDetail(Integer importId) {
         Import anImport = importRepository.findById(importId).orElseThrow(
                () -> new RuntimeException("Import not found")
        );

        return importMapper.toImportDetailResponse(anImport);
    }
}
