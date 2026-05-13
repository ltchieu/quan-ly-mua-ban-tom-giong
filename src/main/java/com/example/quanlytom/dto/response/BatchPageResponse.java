package com.example.quanlytom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BatchPageResponse implements Serializable {
    long totalItems;
    int currentPage;
    int totalPages;
    List<BatchResponse> batchList;

    @Data
    public static class BatchResponse{
        private Integer id;
        private String batchName;
        private LocalDateTime createdDate;
        private String status;
    }
}
