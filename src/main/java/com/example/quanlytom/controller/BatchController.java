package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.BatchUpdateRequest;
import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.BatchDetailResponse;
import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.dto.response.BatchPageResponse;
import com.example.quanlytom.service.BatchService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/batch")
public class BatchController {
    final BatchService batchService;

    @GetMapping("/batch-name")
    public ResponseEntity<ApiResponse<List<BatchNameResponse>>> getAllBatchName() {
        List<BatchNameResponse> responseList = batchService.getAllBatchNames();
        return ResponseEntity.ok().body(ApiResponse.<List<BatchNameResponse>>builder().data(responseList).build());
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<BatchPageResponse>> getAllBatch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        BatchPageResponse response = batchService.getAllBatch(page, size, startDate, endDate);
        return ResponseEntity.ok().body(ApiResponse.<BatchPageResponse>builder().data(response).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchDetailResponse>> getDetailBatch(@PathVariable Integer id) {
        BatchDetailResponse batchDetail = batchService.getDetailResponse(id);
        return ResponseEntity.ok().body(ApiResponse.<BatchDetailResponse>builder().data(batchDetail).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchPageResponse.BatchResponse>> updateBatch(
            @PathVariable Integer id,
            @RequestBody BatchUpdateRequest batchUpdateRequest
    ){
        BatchPageResponse.BatchResponse response = batchService.updateBatch(id, batchUpdateRequest);
        return ResponseEntity.ok().body(ApiResponse.<BatchPageResponse.BatchResponse>builder().data(response).build());
    }

    @PutMapping("/change-status-canceled/{id}")
    public ResponseEntity<ApiResponse<Void>> changeStatusToCanceled(
            @PathVariable Integer id
    ){
        batchService.changBatchStatusToCanceled(id);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().build());
    }
}
