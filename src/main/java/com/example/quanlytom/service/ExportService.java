package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.ExportCreationRequest;
import com.example.quanlytom.dto.request.ExportUpdateRequest;
import com.example.quanlytom.dto.response.ExportDetailResponse;
import com.example.quanlytom.dto.response.ExportPageResponse;
import com.example.quanlytom.entity.Export;
import com.example.quanlytom.entity.ExportDetail;
import com.example.quanlytom.mapper.ExportDetailMapper;
import com.example.quanlytom.mapper.ExportMapper;
import com.example.quanlytom.repository.CustomerRepository;
import com.example.quanlytom.repository.ExportDetailRepository;
import com.example.quanlytom.repository.ExportRepository;
import com.example.quanlytom.repository.ImportDetailRepository;
import com.example.quanlytom.repository.InventoryRepository;
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
    final CustomerRepository customerRepository;
    final ExportRepository exportRepository;
    final ExportDetailRepository exportDetailRepository;
    final ImportDetailRepository importDetailRepository;
    final InventoryRepository inventoryRepository;
    final InventoryService inventoryService;
    final ExportMapper exportMapper;
    final ExportDetailMapper exportDetailMapper;

    private Integer requireImportDetailId(ExportCreationRequest.ExportDetailCreationRequest item) {
        if (item.getImportDetailId() != null) {
            return item.getImportDetailId();
        }
        throw new RuntimeException("importDetailId is required for export detail");
    }

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

        if (exportCreationRequest.getCustomerId() != null) {
            var customer = customerRepository.findById(exportCreationRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + exportCreationRequest.getCustomerId()));
            newExport.setCustomer(customer);
        }

        exportRepository.save(newExport);

        if(exportCreationRequest.getExportDetails() != null){
            for (var item : exportCreationRequest.getExportDetails()){
                ExportDetail exportDetail = exportDetailMapper.toExportDetail(item);
                exportDetail.setExport(newExport);

                Integer inventoryId = requireImportDetailId(item);
                var inventory = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new RuntimeException("Inventory not found (sent as importDetailId): " + inventoryId));

                Integer batchId = inventory.getBatch().getId();
                Integer shrimpAttrId = inventory.getShrimpAttribute().getId();
                
                var importDetails = importDetailRepository.findByBatch_IdAndShrimpAttribute_Id(batchId, shrimpAttrId);
                if(importDetails.isEmpty()) throw new RuntimeException("No import detail matches for inventory: " + inventoryId);
                
                exportDetail.setImportDetail(importDetails.getFirst());
                exportDetailRepository.save(exportDetail);

                inventoryService.updateStockQuantity(batchId, shrimpAttrId, exportDetail.getActualQuantity());
            }
        }

        return exportMapper.toExportResponse(newExport);
    }

    @Transactional
    public ExportPageResponse.ExportResponse updateExport(ExportUpdateRequest exportUpdateRequest, Integer exportId){
        Export anExport = exportRepository.findById(exportId).orElseThrow(() -> new RuntimeException("Export not found"));
        exportMapper.updateExportFromRequest(exportUpdateRequest, anExport);

        if (exportUpdateRequest.getCustomerId() != null) {
            var customer = customerRepository.findById(exportUpdateRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + exportUpdateRequest.getCustomerId()));
            anExport.setCustomer(customer);
        }

        exportRepository.save(anExport);

        if (exportUpdateRequest.getExportDetails() != null){
            List<ExportDetail> currentDetails = new ArrayList<>(anExport.getExportDetails());

            for (var item : exportUpdateRequest.getExportDetails()){
                Integer detailId = item.getExportDetailId();
                ExportDetail existingDetail = null;

                if (detailId != null) {
                    existingDetail = exportDetailRepository.findById(detailId)
                            .orElseThrow(() -> new RuntimeException("ExportDetail not found: " + detailId));
                    if (existingDetail.getExport() == null || !exportId.equals(existingDetail.getExport().getId())) {
                        throw new RuntimeException("ExportDetail not found for export: " + detailId);
                    }
                }

                if (existingDetail != null) {
                    Double oldQuantity = existingDetail.getActualQuantity();
                    Integer oldBatchId = existingDetail.getImportDetail().getBatch().getId();
                    Integer oldShrimpAttrId = existingDetail.getImportDetail().getShrimpAttribute().getId();

                    exportDetailMapper.updateExportDetailFromRequest(item, existingDetail);

                    if (item.getImportDetailId() != null) {
                        Integer inventoryId = item.getImportDetailId();
                        var inventory = inventoryRepository.findById(inventoryId)
                                .orElseThrow(() -> new RuntimeException("Inventory not found: " + inventoryId));
                        
                        Integer newBatchId = inventory.getBatch().getId();
                        Integer newShrimpAttrId = inventory.getShrimpAttribute().getId();

                        if (!oldBatchId.equals(newBatchId) || !oldShrimpAttrId.equals(newShrimpAttrId)) {
                            inventoryService.adjustStockQuantity(oldBatchId, oldShrimpAttrId, oldQuantity);
                            inventoryService.updateStockQuantity(newBatchId, newShrimpAttrId, existingDetail.getActualQuantity());
                            
                            var importDetails = importDetailRepository.findByBatch_IdAndShrimpAttribute_Id(newBatchId, newShrimpAttrId);
                            if(importDetails.isEmpty()) throw new RuntimeException("No import detail found");
                            existingDetail.setImportDetail(importDetails.getFirst());
                        } else {
                            double delta = existingDetail.getActualQuantity() - oldQuantity;
                            inventoryService.adjustStockQuantity(newBatchId, newShrimpAttrId, -delta);
                        }
                    } else {
                        double delta = existingDetail.getActualQuantity() - oldQuantity;
                        inventoryService.adjustStockQuantity(oldBatchId, oldShrimpAttrId, -delta);
                    }

                    exportDetailRepository.save(existingDetail);
                    currentDetails.remove(existingDetail);
                } else {
                    ExportDetail newDetail = exportDetailMapper.toExportDetail(item);
                    newDetail.setExport(anExport);

                    Integer inventoryId = item.getImportDetailId();
                    if (inventoryId == null) {
                        throw new RuntimeException("importDetailId (inventoryId) is required when creating a new export detail");
                    }
                    var inventory = inventoryRepository.findById(inventoryId)
                            .orElseThrow(() -> new RuntimeException("Inventory not found: " + inventoryId));

                    Integer batchId = inventory.getBatch().getId();
                    Integer shrimpAttrId = inventory.getShrimpAttribute().getId();
                    var importDetails = importDetailRepository.findByBatch_IdAndShrimpAttribute_Id(batchId, shrimpAttrId);
                    if(importDetails.isEmpty()) throw new RuntimeException("No import detail found");
                    newDetail.setImportDetail(importDetails.getFirst());

                    exportDetailRepository.save(newDetail);
                    inventoryService.updateStockQuantity(batchId, shrimpAttrId, newDetail.getActualQuantity());
                }
            }

            if (!currentDetails.isEmpty()) {
                for (ExportDetail delItem : currentDetails) {
                    Integer bId = delItem.getImportDetail().getBatch().getId();
                    Integer sId = delItem.getImportDetail().getShrimpAttribute().getId();
                    inventoryService.adjustStockQuantity(bId, sId, delItem.getActualQuantity());
                }
                exportDetailRepository.deleteAll(currentDetails);
            }
        }

        return exportMapper.toExportResponse(anExport);
    }

    @Transactional
    public void deleteExport(Integer exportId) {
        Export anExport = exportRepository.findById(exportId).orElseThrow(() -> new RuntimeException("Export not found"));
        for(ExportDetail ed : anExport.getExportDetails()){
            Integer bId = ed.getImportDetail().getBatch().getId();
            Integer sId = ed.getImportDetail().getShrimpAttribute().getId();
            inventoryService.adjustStockQuantity(bId, sId, ed.getActualQuantity());
        }
        anExport.setDeleted(true);
        anExport.setDeletedAt(LocalDateTime.now());
        exportRepository.save(anExport);
    }
}
