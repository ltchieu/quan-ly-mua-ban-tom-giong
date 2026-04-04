package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.ExportPageResponse;
import com.example.quanlytom.service.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
