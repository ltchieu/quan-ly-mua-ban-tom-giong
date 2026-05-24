package com.example.quanlytom.mapper;

import com.example.quanlytom.dto.request.InventoryCreationRequest;
import com.example.quanlytom.dto.response.InventoryDetailResponse;
import com.example.quanlytom.dto.response.InventoryResponse;
import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "shrimpName", source = "shrimpAttribute.shrimp.name")
    @Mapping(target = "batchName", source = "batch.batchName")
    @Mapping(target = "attributeName", source = "shrimpAttribute.attribute.name")
    InventoryResponse toInventoryResponse(Inventory inventory);

    List<InventoryResponse> toInventoryResponseList(List<Inventory> inventories);

    @Mapping(target = "shrimpName", source = "shrimpAttribute.shrimp.name")
    @Mapping(target = "attributeName", source = "shrimpAttribute.attribute.name")
    @Mapping(target = "shrimpId", source = "shrimpAttribute.shrimp.id")
    @Mapping(target = "attributeId", source = "shrimpAttribute.attribute.id")
    @Mapping(target = "importQuantity", ignore = true)
    @Mapping(target = "exportedQuantity", ignore = true)
    @Mapping(target = "returnedQuantity", ignore = true)
    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "price", ignore = true)
    InventoryDetailResponse toInventoryDetail(Inventory inventory);

    @Mapping(target = "batch", ignore = true)
    @Mapping(target = "shrimpAttribute", ignore = true)
    Inventory toNewInventory(InventoryCreationRequest inventoryCreationRequest);

    @Mapping(target = "importDetailId", source = "id")
    @Mapping(target = "shrimpName", source = "shrimpAttribute.shrimp.name")
    @Mapping(target = "attributeName", source = "shrimpAttribute.attribute.name")
    @Mapping(target = "remainingQuantity", source = "stockQuantity")
    @Mapping(target = "importPrice", ignore = true)
    AvailableStockResponse toAvailableStockResponse(Inventory inventory);

    List<AvailableStockResponse> toAvailableStockResponseList(List<Inventory> inventories);
}
