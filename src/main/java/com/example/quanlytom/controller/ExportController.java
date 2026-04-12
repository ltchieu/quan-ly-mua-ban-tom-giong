package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.ExportCreationRequest;
import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.ExportDetailResponse;
import com.example.quanlytom.dto.response.ExportPageResponse;
import com.example.quanlytom.entity.Export;
import com.example.quanlytom.service.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/exports")
@AllArgsConstructor
public class ExportController {
    final ExportService exportService;

    @GetMapping()
    public ResponseEntity<ApiResponse<ExportPageResponse>> getAllExports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer customerId
    ) {
        ExportPageResponse res = exportService.getAllExports(
                startDate,
                endDate,
                customerId,
                page,
                size
        );
        return ResponseEntity.ok().body(ApiResponse.<ExportPageResponse>builder().data(res).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExportDetailResponse>> getDetailsExport(@PathVariable int id) {
        ExportDetailResponse res = exportService.getDetailsExport(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.<ExportDetailResponse>builder()
                                .data(res)
                                .build()
                );
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ExportPageResponse.ExportResponse>> createNewExport(
            @RequestBody ExportCreationRequest exportCreationRequest
    ){
        ExportPageResponse.ExportResponse exportResponse = exportService.saveExport(exportCreationRequest);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<ExportPageResponse.ExportResponse>builder()
                                .data(exportResponse)
                                .build()
                );
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<ExportPageResponse.ExportResponse>> updateAnExport(
            @RequestBody ExportCreationRequest exportUpdateRequest,
            @PathVariable Integer id
    ) {
        ExportPageResponse.ExportResponse updatedExport = exportService.updateExport(exportUpdateRequest, id);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<ExportPageResponse.ExportResponse>builder()
                                .data(updatedExport)
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnExport(@PathVariable Integer id) {
        exportService.deleteExport(id);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<Void>builder()
                                .message("Export deleted successfully")
                                .build()
                );
    }
}
