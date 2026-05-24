package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.BatchStatusResponse;
import com.example.quanlytom.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Page<Batch> findAll(Specification<Batch> batchSpecification, Pageable paging);

    // --- Statistics queries ---

    @Query(value = "SELECT trang_thai AS status, COUNT(*) AS total FROM lo_hang WHERE is_deleted = 0 GROUP BY trang_thai", nativeQuery = true)
    List<BatchStatusResponse.Projection> getBatchStatusDistribution();
}