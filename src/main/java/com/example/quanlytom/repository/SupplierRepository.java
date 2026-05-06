package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query("""
                select new Supplier(s.id, s.fullName, s.phoneNumber, s.address)
                from Supplier s
                where s.fullName like concat('%', :name, '%')
            """)
    Page<Supplier> findAllByFullName(String name, Pageable pageable);
}