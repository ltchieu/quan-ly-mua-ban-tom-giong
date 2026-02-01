package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.service.ShrimpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shrimps")
@AllArgsConstructor
public class ShrimpController {
    final ShrimpService shrimpService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ShrimpResponse>>> getAllShrimps() {
        List<ShrimpResponse> shrimps = shrimpService.getAllShrimp();
        return ResponseEntity.ok().body(ApiResponse.<List<ShrimpResponse>>builder().data(shrimps).build());
    }
}
