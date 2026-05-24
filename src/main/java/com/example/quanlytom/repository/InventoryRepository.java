package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer>, JpaSpecificationExecutor<Inventory> {
    Inventory findByBatch_IdAndShrimpAttribute_Id(Integer batchId, Integer shrimpAttributeId);
    List<Inventory> findByBatch_Id(Integer batchId);
}