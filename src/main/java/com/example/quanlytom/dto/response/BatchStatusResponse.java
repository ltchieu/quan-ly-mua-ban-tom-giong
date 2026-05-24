package com.example.quanlytom.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchStatusResponse {
    private String status;
    private Long total;

    public interface Projection {
        String getStatus();
        Long getTotal();
    }
}
