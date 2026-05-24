package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    @Query("""
    SELECT new Customer(c.id, c.fullName, c.phoneNumber, c.address)
    FROM Customer c
    WHERE c.fullName LIKE CONCAT('%', :name, '%')
""")
    Page<Customer> findAllByFullName(Pageable pageable, String name);

    // --- Statistics queries ---

    @Query(value = "SELECT COUNT(*) FROM khach_hang WHERE is_deleted = 0", nativeQuery = true)
    Long countAllActive();
}