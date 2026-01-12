package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoHangRepository extends JpaRepository<Batch, Integer> {
}