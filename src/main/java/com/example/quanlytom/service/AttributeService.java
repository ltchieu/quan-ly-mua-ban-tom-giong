package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.AttributeResponse;
import com.example.quanlytom.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;

    public List<AttributeResponse> getAllAttributes() {
        return attributeRepository.findAll().stream().map(attribute -> {
            AttributeResponse response = new AttributeResponse();
            response.setId(attribute.getId());
            response.setNameAttribute(attribute.getName());
            return response;
        }).toList();
    }
}
