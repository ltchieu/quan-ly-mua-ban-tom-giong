package com.example.quanlytom.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class ImportPageResponse implements Serializable {
    List<ImportResponse> imports;
    long totalItems;
    int currentPage;
    int totalPages;
}
