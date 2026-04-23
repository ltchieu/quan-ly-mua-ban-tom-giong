package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Export;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExportRepository extends JpaRepository<Export, Integer> {
    @EntityGraph(attributePaths = {"customer"})
    Page<Export> findAll(Specification<Export> specification, Pageable paging);

    List<Export> findByCustomer_Id(Integer customerId);
}