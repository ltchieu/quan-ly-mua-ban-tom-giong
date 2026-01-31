package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.ImportRequest;
import com.example.quanlytom.dto.response.ImportDetailResponse;
import com.example.quanlytom.dto.response.ImportPageResponse;
import com.example.quanlytom.dto.response.ImportResponse;
import com.example.quanlytom.entity.Batch;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.entity.ImportDetail;
import com.example.quanlytom.entity.ShrimpAttribute;
import com.example.quanlytom.mapper.ImportDetailMapper;
import com.example.quanlytom.repository.BatchRepository;
import com.example.quanlytom.repository.ImportDetailRepository;
import com.example.quanlytom.repository.ImportRepository;
import com.example.quanlytom.repository.ShrimpAttributeRepository;
import com.example.quanlytom.specification.ImportSpec;
import com.example.quanlytom.mapper.ImportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    public final ImportRepository importRepository;
    public final ImportDetailRepository importDetailRepository;
    private final ImportMapper importMapper;
    private final ImportDetailMapper importDetailMapper;
    private final BatchRepository batchRepository;
    private final ShrimpAttributeRepository shrimpAttributeRepository;

    public ImportPageResponse getAllImports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer supplierId,
            int page, int size
    ) {
        Specification<Import> specification = ImportSpec.hasSupplier(supplierId)
                .and(ImportSpec.isBetweenDates(startDate, endDate));

        Pageable paging = PageRequest.of(page, size, Sort.by("importDate").descending());
        Page<Import> pagedResult = importRepository.findAll(specification, paging);

        List<ImportResponse> importResponses = importMapper.toImportResponseList(pagedResult.getContent());
        return new ImportPageResponse(importResponses, pagedResult.getTotalElements(), pagedResult.getNumber(), pagedResult.getTotalPages());
    }

    public ImportDetailResponse getImportDetail(Integer importId) {
        Import anImport = importRepository.findWithDetailsById(importId).orElseThrow(
                () -> new RuntimeException("Import not found")
        );

        return importMapper.toImportDetailResponse(anImport);
    }

    @Transactional
    public ImportDetailResponse saveImport(ImportRequest importRequest) {
        Import anImport = importMapper.toNewImport(importRequest);
        // Ensure fallback if mapper doesn't set these (though it should)
        if (anImport.getImportDate() == null) anImport.setImportDate(LocalDateTime.now());
        if (anImport.getCreatedAt() == null) anImport.setCreatedAt(LocalDateTime.now());
        anImport.setDeleted(false);

        // Save Import first to get the ID
        anImport = importRepository.save(anImport);

        if (importRequest.getImportDetails() != null) {
            for (var detail : importRequest.getImportDetails()) {
                Integer finalBatchId = detail.getBatchId();
                if (finalBatchId == null && detail.getBatchName() != null) {
                    Batch newBatch = new Batch();
                    newBatch.setBatchName(detail.getBatchName());
                    newBatch = batchRepository.save(newBatch);
                    finalBatchId = newBatch.getId();
                }

                ImportDetail importDetail = importDetailMapper.toImportDetail(detail);
                ShrimpAttribute shrimpAttribute = shrimpAttributeRepository.findByShrimpAndAttribute(
                        detail.getShrimpId(),
                        detail.getAttributeId()
                ).orElseThrow(() -> new RuntimeException("ShrimpAttribute not found for shrimpId: " + detail.getShrimpId() + " and attributeId: " + detail.getAttributeId()));
                importDetail.setShrimpAttribute(shrimpAttribute);
                importDetail.setImportOrder(anImport);

                if (finalBatchId != null) {
                    Batch batch = new Batch();
                    batch.setId(finalBatchId);
                    importDetail.setBatch(batch);
                }
                importDetailRepository.save(importDetail);
            }
        }
        return importMapper.toImportDetailResponse(anImport);
    }

    public ImportDetailResponse updateImport(ImportRequest importRequest, Integer importId) {
        Import anImport = importRepository.findById(importId).orElseThrow(() -> new RuntimeException("Import not found"));
        importMapper.updateImportFromRequest(importRequest, anImport);
        importRepository.save(anImport);
        return importMapper.toImportDetailResponse(anImport);
    }

    public void deleteImport(Integer importId) {
        Import anImport = importRepository.findById(importId).orElseThrow(() -> new RuntimeException("Import not found"));
        anImport.setDeleted(true);
        anImport.setDeletedAt(LocalDateTime.now());
        importRepository.save(anImport);
    }
}
