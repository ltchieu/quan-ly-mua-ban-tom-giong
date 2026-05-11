package com.example.quanlytom.controller;

import com.example.quanlytom.dto.response.ApiResponse;
import com.example.quanlytom.dto.response.ShrimpDetailResponse;
import com.example.quanlytom.dto.response.ShrimpPageResponse;
import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.service.ShrimpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<ShrimpPageResponse>> getAllShrimpPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false, defaultValue = "") String shrimpName
    ) {
        ShrimpPageResponse response = shrimpService.getAllShrimpPaginated(page, size, shrimpName);
        return ResponseEntity.ok().body(ApiResponse.<ShrimpPageResponse>builder().data(response).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShrimpDetailResponse>> getDetailShrimp(@PathVariable Integer id) {
        ShrimpDetailResponse response = shrimpService.getDetailShrimp(id);
        return ResponseEntity.ok().body(ApiResponse.<ShrimpDetailResponse>builder().data(response).build());
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ShrimpResponse>> createShrimp(
            @RequestBody String shrimpName
    ) {
        ShrimpResponse newShrimp = shrimpService.createShrimp(shrimpName);
        return ResponseEntity.ok().body(ApiResponse.<ShrimpResponse>builder().data(newShrimp).build());
    }
}
