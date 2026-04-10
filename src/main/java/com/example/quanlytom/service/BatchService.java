package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.dto.response.BatchNameResponse;
import com.example.quanlytom.mapper.BatchMapper;
import com.example.quanlytom.repository.BatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BatchService {
    final BatchRepository batchRepository;
    final BatchMapper batchMapper;

    public List<BatchNameResponse> getAllBatchNames(){
        return batchMapper.toBatchNameResponseList(batchRepository.findAll());
    }
}
