package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.ShrimpDetailResponse;
import com.example.quanlytom.dto.response.ShrimpPageResponse;
import com.example.quanlytom.dto.response.ShrimpResponse;
import com.example.quanlytom.entity.Shrimp;
import com.example.quanlytom.mapper.ShrimpMapper;
import com.example.quanlytom.repository.ShrimpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ShrimpPageResponse getAllShrimpPaginated(int page, int size, String shrimpName){
        Pageable paging = PageRequest.of(page, size);
        Page<Shrimp> shrimpPage = shrimpRepository.findAllByNameContaining(shrimpName, paging);

        List<ShrimpResponse> shrimpList = shrimpMapper.toShrimpResponseList(shrimpPage.getContent());
        return new ShrimpPageResponse(shrimpPage.getTotalElements(), shrimpPage.getNumber(), shrimpPage.getTotalPages(), shrimpList);
    }

    public ShrimpDetailResponse getDetailShrimp(Integer shrimpId){
        Shrimp shrimp = shrimpRepository.findById(shrimpId).orElseThrow(
                () -> new RuntimeException("Shrimp not found")
        );
        ShrimpDetailResponse.ShrimpStatistics statistics = shrimpRepository.getShrimpStatistics(shrimpId);
        return shrimpMapper.toShrimpDetailResponse(shrimp, statistics);
    }

    public ShrimpResponse createShrimp(String shrimpName){
        Shrimp shrimp = new Shrimp();
        shrimp.setName(shrimpName);
        shrimp.setDeleted(false);

        shrimpRepository.save(shrimp);
        return shrimpMapper.toShrimpResponse(shrimp);
    }
}
