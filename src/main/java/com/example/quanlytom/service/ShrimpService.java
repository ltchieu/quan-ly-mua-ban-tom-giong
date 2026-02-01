package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.repository.ShrimpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShrimpService {
    private final ShrimpRepository shrimpRepository;

    public List<ShrimpResponse> getAllShrimp() {
        return shrimpRepository.findAll().stream().map(shrimp -> {
            ShrimpResponse response = new ShrimpResponse();
            response.setId(shrimp.getId());
            response.setNameShrimp(shrimp.getName());
            return response;
        }).toList();
    }
}
