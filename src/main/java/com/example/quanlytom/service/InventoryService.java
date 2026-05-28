package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.InventoryCreationRequest;
import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.dto.response.InventoryDetailResponse;
import com.example.quanlytom.dto.response.InventoryPageResponse;
import com.example.quanlytom.dto.response.InventoryResponse;
import com.example.quanlytom.entity.Batch;
import com.example.quanlytom.entity.Inventory;
import com.example.quanlytom.mapper.BatchMapper;
import com.example.quanlytom.mapper.InventoryMapper;
import com.example.quanlytom.repository.BatchRepository;
import com.example.quanlytom.repository.ExportDetailRepository;
import com.example.quanlytom.repository.ImportDetailRepository;
import com.example.quanlytom.repository.InventoryRepository;
import com.example.quanlytom.specification.GenericSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {
    final InventoryRepository inventoryRepository;
    final InventoryMapper inventoryMapper;
    final ImportDetailRepository importDetailRepository;
    final ExportDetailRepository exportDetailRepository;
    final BatchRepository batchRepository;
    final BatchMapper batchMapper;

    public InventoryPageResponse getAllInventories(
            int page, int size,
            Integer batchId,
            Integer shrimpId,
            Integer attributeId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){
        Specification<Inventory> specification = GenericSpecification.<Inventory>isBetweenDates(startDate, endDate, "updatedAt")
                .and(GenericSpecification.hasJoinAttribute("batch", batchId));

        if (shrimpId != null) {
            specification = specification.and((root, query, cb) -> 
                    cb.equal(root.get("shrimpAttribute").get("shrimp").get("id"), shrimpId));
        }

        if (attributeId != null) {
            specification = specification.and((root, query, cb) -> 
                    cb.equal(root.get("shrimpAttribute").get("attribute").get("id"), attributeId));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Inventory> inventoryPage = inventoryRepository.findAll(specification, pageable);

        List<InventoryResponse> inventories = inventoryMapper.toInventoryResponseList(inventoryPage.getContent());
        return new InventoryPageResponse(inventoryPage.getTotalElements(), inventoryPage.getNumber(), inventoryPage.getTotalPages(), inventories);
    }

    public InventoryDetailResponse getDetailInventory(Integer inventoryId){
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found"));
        InventoryDetailResponse detailResponse = inventoryMapper.toInventoryDetail(inventory);

        Integer batchId = inventory.getBatch().getId();
        Integer shrimpAttributeId = inventory.getShrimpAttribute().getId();

        Double importQuantity = importDetailRepository.sumImportQuantity(batchId, shrimpAttributeId);
        Double exportQuantity = exportDetailRepository.sumExportedQuantity(batchId, shrimpAttributeId);
        Double returnedQuantity = exportDetailRepository.sumReturnedQuantity(batchId, shrimpAttributeId);
        
        Double importPrice = importDetailRepository.getLatestImportPrice(batchId, shrimpAttributeId);
        Double totalImportCost = importDetailRepository.sumTotalImportCost(batchId, shrimpAttributeId);

        Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch can not found"));
        double deadOrLost = (importQuantity - exportQuantity + returnedQuantity) - inventory.getStockQuantity();

        detailResponse.setDeadOrLostQuantity(Math.max(0.0, deadOrLost));
        detailResponse.setBatch(batchMapper.toBatchResponse(batch));
        detailResponse.setImportQuantity(importQuantity);
        detailResponse.setExportedQuantity(exportQuantity);
        detailResponse.setReturnedQuantity(returnedQuantity);

        InventoryDetailResponse.PriceInfo priceInfo = new InventoryDetailResponse.PriceInfo();
        priceInfo.setImportPrice(BigDecimal.valueOf(importPrice));
        priceInfo.setTotalImportCost(totalImportCost);
        priceInfo.setEstimatedValue(BigDecimal.valueOf(inventory.getStockQuantity() * importPrice));
        detailResponse.setPrice(priceInfo);

        return detailResponse;
    }

    public void addNewStock(InventoryCreationRequest inventoryCreationRequest){
        Inventory anInventory = inventoryRepository.findByBatch_IdAndShrimpAttribute_Id(
                inventoryCreationRequest.getBatch().getId(), 
                inventoryCreationRequest.getShrimpAttribute().getId());
        
        if (anInventory != null) {
            anInventory.setStockQuantity(anInventory.getStockQuantity() + inventoryCreationRequest.getStockQuantity());
            anInventory.setUpdatedAt(LocalDateTime.now());
            if (anInventory.getStoredAt() == null) {
                anInventory.setStoredAt(inventoryCreationRequest.getStoredAt() != null ? inventoryCreationRequest.getStoredAt() : LocalDateTime.now());
            }
            inventoryRepository.save(anInventory);
        } else {
            Inventory newInventory = inventoryMapper.toNewInventory(inventoryCreationRequest);
            newInventory.setBatch(inventoryCreationRequest.getBatch());
            newInventory.setShrimpAttribute(inventoryCreationRequest.getShrimpAttribute());
            newInventory.setUpdatedAt(LocalDateTime.now());
            newInventory.setStoredAt(inventoryCreationRequest.getStoredAt() != null ? inventoryCreationRequest.getStoredAt() : LocalDateTime.now());

            inventoryRepository.save(newInventory);
        }
    }

    public void adjustStockQuantity(Integer batchId, Integer shrimpAttributeId, Double quantityDifference){
        Inventory anInventory = inventoryRepository.findByBatch_IdAndShrimpAttribute_Id(batchId, shrimpAttributeId);
        if(anInventory != null){
            anInventory.setStockQuantity(anInventory.getStockQuantity() + quantityDifference);
            anInventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(anInventory);
        }
    }

    public void updateStockQuantity(Integer batchId, Integer shrimpAttributeId, Double quantity){
        Inventory anInventory = inventoryRepository.findByBatch_IdAndShrimpAttribute_Id(batchId, shrimpAttributeId);
        if(anInventory == null) throw new RuntimeException("Inventory not found");

        Double currQuantity = anInventory.getStockQuantity();
        if(currQuantity < quantity) throw new RuntimeException("Export quantity is larger than stock quantity");
        anInventory.setStockQuantity(currQuantity - quantity);
        anInventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(anInventory);
    }

    public List<AvailableStockResponse> getAvailableStock(Integer batchId){
        List<Inventory> inventories = inventoryRepository.findByBatch_Id(batchId);
        List<AvailableStockResponse> responses = new ArrayList<>();
        for (Inventory inventory : inventories) {
            AvailableStockResponse response = inventoryMapper.toAvailableStockResponse(inventory);
            Double importPrice = importDetailRepository.getLatestImportPrice(batchId, inventory.getShrimpAttribute().getId());
            response.setImportPrice(importPrice != null ? importPrice : 0.0);
            responses.add(response);
        }
        return responses;
    }
}
