package com.example.quanlytom.dto.request;

import com.example.quanlytom.enums.BatchStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class BatchUpdateRequest implements Serializable {
    String batchName;
    BatchStatus status;
}
