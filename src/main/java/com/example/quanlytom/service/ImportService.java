package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.ImportCreationRequest;
import com.example.quanlytom.dto.request.InventoryCreationRequest;
import com.example.quanlytom.dto.response.ImportDetailResponse;
import com.example.quanlytom.dto.response.ImportPageResponse;
import com.example.quanlytom.dto.response.ImportResponse;
import com.example.quanlytom.entity.Batch;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.entity.ImportDetail;
import com.example.quanlytom.entity.ShrimpAttribute;
import com.example.quanlytom.enums.BatchStatus;
import com.example.quanlytom.mapper.ImportDetailMapper;
import com.example.quanlytom.repository.*;
import com.example.quanlytom.specification.GenericSpecification;
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
import java.util.ArrayList;
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
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;
    private final UserService userService;

    public ImportPageResponse getAllImports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer supplierId,
            int page, int size
    ) {
        Specification<Import> specification = GenericSpecification.<Import>isNotDeleted()
                .and(GenericSpecification.hasJoinAttribute("supplier", supplierId))
                .and(GenericSpecification.isBetweenDates(startDate, endDate, "importDate"));

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
    public ImportDetailResponse saveImport(ImportCreationRequest importCreationRequest) {
        Import anImport = importMapper.toNewImport(importCreationRequest);
        // Ensure fallback if mapper doesn't set these
        if (anImport.getImportDate() == null) anImport.setImportDate(LocalDateTime.now());
        if (anImport.getCreatedAt() == null) anImport.setCreatedAt(LocalDateTime.now());
        anImport.setDeleted(false);

        com.example.quanlytom.entity.Users currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            anImport.setCreatedBy(currentUser);
        } else if (importCreationRequest.getEmployeeId() != null) {
            userService.getUserById(importCreationRequest.getEmployeeId())
                    .ifPresent(anImport::setCreatedBy);
        }

        // Save Import first to get the ID
        anImport = importRepository.save(anImport);

        //Save a new Batch
        Batch newBatch = new Batch();
        String batchName = "BATCH-" + anImport.getCreatedAt().toLocalDate().toString();
        newBatch.setBatchName(batchName);
        newBatch.setCreatedDate(LocalDateTime.now());
        newBatch.setStatus(BatchStatus.IN_PROGRESS);
        newBatch = batchRepository.save(newBatch);

        // Save ImportDetails
        if (importCreationRequest.getImportDetails() != null) {
            for (var detail : importCreationRequest.getImportDetails()) {
                ImportDetail importDetail = importDetailMapper.toImportDetail(detail);
                ShrimpAttribute shrimpAttribute = shrimpAttributeRepository.findByShrimpAndAttribute(
                        detail.getShrimpId(),
                        detail.getAttributeId()
                ).orElseThrow(() -> new RuntimeException("ShrimpAttribute not found for shrimpId: " + detail.getShrimpId() + " and attributeId: " + detail.getAttributeId()));
                importDetail.setShrimpAttribute(shrimpAttribute);
                importDetail.setImportOrder(anImport);
                importDetail.setBatch(newBatch);

                importDetailRepository.save(importDetail);

                inventoryService.addNewStock(new InventoryCreationRequest(importDetail.getQuantity(), newBatch, shrimpAttribute, anImport.getCreatedAt()));
            }
        }
        return importMapper.toImportDetailResponse(anImport);
    }

    @Transactional
    public ImportDetailResponse updateImport(ImportCreationRequest importCreationRequest, Integer importId) {
        Import anImport = importRepository.findById(importId)
                .orElseThrow(() -> new RuntimeException("Import not found"));
        importMapper.updateImportFromRequest(importCreationRequest, anImport);

        com.example.quanlytom.entity.Users currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            anImport.setCreatedBy(currentUser);
        } else if (importCreationRequest.getEmployeeId() != null) {
            userService.getUserById(importCreationRequest.getEmployeeId())
                    .ifPresent(anImport::setCreatedBy);
        }

        if (importCreationRequest.getSupplierId() != null) {
            var supplier = supplierRepository.findById(importCreationRequest.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found: " + importCreationRequest.getSupplierId()));
            anImport.setSupplier(supplier);
        }

        importRepository.save(anImport);

        if (importCreationRequest.getImportDetails() != null) {
            List<ImportDetail> currentDetails = new ArrayList<>(anImport.getImportDetails());
            Batch importBatch = currentDetails.isEmpty() ? null : currentDetails.getFirst().getBatch();

            for (var item : importCreationRequest.getImportDetails()) {
                ImportDetail existingDetail = null;

                if (item.getId() != null) {
                    existingDetail = currentDetails.stream()
                            .filter(d -> item.getId().equals(d.getId()))
                            .findFirst()
                            .orElse(null);
                }

                if (existingDetail != null) {
                    Double oldQuantity = existingDetail.getQuantity();
                    ShrimpAttribute oldShrimpAttr = existingDetail.getShrimpAttribute();
                    Batch detailBatch = existingDetail.getBatch();
                    if (importBatch == null) importBatch = detailBatch;

                    // Update existing detail
                    importDetailMapper.updateImportDetailFromRequest(item, existingDetail);

                    // Update ShrimpAttribute nếu có thay đổi shrimpId hoặc attributeId
                    ShrimpAttribute newShrimpAttr = oldShrimpAttr;
                    if (item.getShrimpId() != null || item.getAttributeId() != null) {
                        Integer shrimpId = item.getShrimpId() != null
                                ? item.getShrimpId()
                                : oldShrimpAttr.getShrimp().getId();
                        Integer attributeId = item.getAttributeId() != null
                                ? item.getAttributeId()
                                : oldShrimpAttr.getAttribute().getId();

                        newShrimpAttr = shrimpAttributeRepository
                                .findByShrimpAndAttribute(shrimpId, attributeId)
                                .orElseThrow(() -> new RuntimeException(
                                        "ShrimpAttribute not found for shrimpId: " + shrimpId + ", attributeId: " + attributeId));
                        existingDetail.setShrimpAttribute(newShrimpAttr);
                    }

                    if (oldShrimpAttr.getId().equals(newShrimpAttr.getId())) {
                        double difference = existingDetail.getQuantity() - oldQuantity;
                        if (difference != 0) {
                            inventoryService.adjustStockQuantity(detailBatch.getId(), oldShrimpAttr.getId(), difference);
                        }
                    } else {
                        inventoryService.adjustStockQuantity(detailBatch.getId(), oldShrimpAttr.getId(), -oldQuantity);
                        inventoryService.addNewStock(new InventoryCreationRequest(existingDetail.getQuantity(), detailBatch, newShrimpAttr, anImport.getCreatedAt()));
                    }

                    importDetailRepository.save(existingDetail);
                    currentDetails.remove(existingDetail);
                } else {
                    // Create new detail
                    if (item.getShrimpId() == null || item.getAttributeId() == null) {
                        throw new RuntimeException("shrimpId and attributeId are required when creating a new import detail");
                    }

                    ShrimpAttribute shrimpAttribute = shrimpAttributeRepository
                            .findByShrimpAndAttribute(item.getShrimpId(), item.getAttributeId())
                            .orElseThrow(() -> new RuntimeException(
                                    "ShrimpAttribute not found for shrimpId: " + item.getShrimpId() + ", attributeId: " + item.getAttributeId()));

                    ImportDetail newDetail = importDetailMapper.toImportDetail(item);
                    newDetail.setImportOrder(anImport);
                    newDetail.setShrimpAttribute(shrimpAttribute);
                    
                    if (importBatch != null) {
                        newDetail.setBatch(importBatch);
                    } else {
                        // Fallback in case import previously had no details (unexpected)
                        Batch newBatch = new Batch();
                        newBatch.setBatchName("BATCH-" + anImport.getCreatedAt().toLocalDate().toString());
                        newBatch.setCreatedDate(LocalDateTime.now());
                        newBatch.setStatus(BatchStatus.IN_PROGRESS);
                        importBatch = batchRepository.save(newBatch);
                        newDetail.setBatch(importBatch);
                    }
                    
                    importDetailRepository.save(newDetail);
                    
                    inventoryService.addNewStock(new InventoryCreationRequest(newDetail.getQuantity(), importBatch, shrimpAttribute, anImport.getCreatedAt()));
                    
                    // Attach to anImport to fix duplicated or missing relation issues
                    anImport.getImportDetails().add(newDetail);
                }
            }

            // Xóa các detail không còn trong request
            if (!currentDetails.isEmpty()) {
                for (ImportDetail deletedDetail : currentDetails) {
                    inventoryService.adjustStockQuantity(deletedDetail.getBatch().getId(), deletedDetail.getShrimpAttribute().getId(), -deletedDetail.getQuantity());
                }
                importDetailRepository.deleteAll(currentDetails);
            }
        }

        return importMapper.toImportDetailResponse(anImport);
    }

    public void deleteImport(Integer importId) {
        Import anImport = importRepository.findById(importId).orElseThrow(() -> new RuntimeException("Import not found"));
        anImport.setDeleted(true);
        anImport.setDeletedAt(LocalDateTime.now());
        
        com.example.quanlytom.entity.Users currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            anImport.setCreatedBy(currentUser);
        }
        
        importRepository.save(anImport);
    }
}
