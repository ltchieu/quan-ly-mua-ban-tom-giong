package com.example.quanlytom.controller;

import com.example.quanlytom.dto.request.ImportRequest;
import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.ImportDetailResponse;
import com.example.quanlytom.dto.response.ImportPageResponse;
import com.example.quanlytom.dto.response.ImportResponse;
import com.example.quanlytom.repository.ImportRepository;
import com.example.quanlytom.service.ImportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/imports")
@AllArgsConstructor
public class ImportController {
    private final ImportService importService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllImports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer supplierId
    ) {
        ImportPageResponse res = importService.getAllImports(
                startDate,
                endDate,
                supplierId,
                page,
                size
        );
        return ResponseEntity.ok().body(ApiResponse.builder().data(res).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getImportDetail(@PathVariable Integer importId) {
        ImportDetailResponse res = importService.getImportDetail(importId);
        return ResponseEntity.ok().body(ApiResponse.builder().data(res).build());
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> saveImport(@RequestBody ImportRequest importRequest) {
        ImportDetailResponse anImport = importService.saveImport(importRequest);
        return ResponseEntity.ok().body(ApiResponse.builder().data(anImport).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateImport(@PathVariable Integer id, @RequestBody ImportRequest importRequest) {
        ImportDetailResponse updatedImport = importService.updateImport(importRequest, id);
        return ResponseEntity.ok().body(ApiResponse.builder().data(updatedImport).build());
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteImport(@PathVariable Integer id) {
        importService.deleteImport(id);
        return ResponseEntity.ok().body(ApiResponse.builder().message("Import deleted successfully").build());
    }
}
