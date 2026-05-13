package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Page<Batch> findAll(Specification<Batch> batchSpecification, Pageable paging);
}