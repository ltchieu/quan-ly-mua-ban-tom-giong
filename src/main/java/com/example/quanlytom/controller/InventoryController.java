package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.dto.response.InventoryDetailResponse;
import com.example.quanlytom.dto.response.InventoryPageResponse;
import com.example.quanlytom.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventories")
@AllArgsConstructor
public class InventoryController {
    final InventoryService inventoryService;

    @GetMapping()
    public ResponseEntity<ApiResponse<InventoryPageResponse>> getAllInventory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false) Integer batchId,
        @RequestParam(required = false) Integer shrimpId,
        @RequestParam(required = false) Integer attributeId
    ){
        InventoryPageResponse response = inventoryService.getAllInventories(page, size, batchId, shrimpId, attributeId, startDate, endDate);
        return ResponseEntity.ok().body(ApiResponse.<InventoryPageResponse>builder().data(response).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDetailResponse>> getDetailInventory(@PathVariable Integer id){
        InventoryDetailResponse detailResponse = inventoryService.getDetailInventory(id);
        return ResponseEntity.ok().body(ApiResponse.<InventoryDetailResponse>builder().data(detailResponse).build());

    }

    @GetMapping("available-stock/{batchId}")
    public ResponseEntity<ApiResponse<List<AvailableStockResponse>>> getAvailableStock(@PathVariable Integer batchId){
        List<AvailableStockResponse> res = inventoryService.getAvailableStock(batchId);
        return ResponseEntity.ok().body(ApiResponse.<List<AvailableStockResponse>>builder().data(res).build());
    }
}
