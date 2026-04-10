package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.service.BatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/batch")
public class BatchController {
    final BatchService batchService;

    @GetMapping("/batch-name")
    public ResponseEntity<ApiResponse<List<BatchNameResponse>>> getAllBatchName(){
        List<BatchNameResponse> responseList = batchService.getAllBatchNames();
        return ResponseEntity.ok().body(ApiResponse.<List<BatchNameResponse>>builder().data(responseList).build());
    }
}
