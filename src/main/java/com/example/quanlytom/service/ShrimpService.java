package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.mapper.ShrimpMapper;
import com.example.quanlytom.repository.ShrimpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShrimpService {
    private final ShrimpRepository shrimpRepository;
    private final ShrimpMapper shrimpMapper;

    public List<ShrimpResponse> getAllShrimp() {
        return shrimpMapper.toShrimpResponseList(shrimpRepository.findAll());
    }
}
