package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.AttributeResponse;
import com.example.quanlytom.service.AttributeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attributes")
@AllArgsConstructor
public class AttributeController {

    final AttributeService attributeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAllAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok().body(ApiResponse.<List<AttributeResponse>>builder().data(attributes).build());
    }
}
