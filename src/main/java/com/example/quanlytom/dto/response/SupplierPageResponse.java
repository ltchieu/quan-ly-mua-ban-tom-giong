package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SupplierPageResponse {
    long totalItems;
    int currentPage;
    int totalPages;
    List<SupplierResponse> suppliers;
}
