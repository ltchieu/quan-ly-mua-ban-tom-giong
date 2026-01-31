package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Import;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImportRepository extends JpaRepository<Import, Integer>, JpaSpecificationExecutor<Import> {

    @Override
    @EntityGraph(attributePaths = {"supplier"})
    Page<Import> findAll(Specification<Import> spec, Pageable pageable);

    @EntityGraph(attributePaths = {
            "supplier",
            "importDetails",
            "importDetails.batch",
            "importDetails.shrimpAttribute",
            "importDetails.shrimpAttribute.shrimp",
            "importDetails.shrimpAttribute.attribute"
    })
    @Query("SELECT i FROM Import i WHERE i.id = :id")
    Optional<Import> findWithDetailsById(@Param("id") Integer id);
}