package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImportRepository extends JpaRepository<Import, Integer>, JpaSpecificationExecutor<Import> {
}