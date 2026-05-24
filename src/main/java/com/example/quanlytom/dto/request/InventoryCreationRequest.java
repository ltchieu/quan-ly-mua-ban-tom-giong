package com.example.quanlytom.dto.request;

import com.example.quanlytom.entity.Batch;
import com.example.quanlytom.entity.ShrimpAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryCreationRequest {
    Double stockQuantity;
    Batch batch;
    ShrimpAttribute shrimpAttribute;
}
