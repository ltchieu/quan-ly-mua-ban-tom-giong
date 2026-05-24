package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class InventoryPageResponse implements Serializable {
    long totalItems;
    int currentPage;
    int totalPages;

    List<InventoryResponse> inventories;
}
