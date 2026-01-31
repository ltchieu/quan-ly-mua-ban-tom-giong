package com.example.quanlytom.repository;

import com.example.quanlytom.entity.Attribute;
import com.example.quanlytom.entity.ShrimpAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ShrimpAttributeRepository extends JpaRepository<ShrimpAttribute, Integer> {
  @Query("SELECT sa FROM ShrimpAttribute sa WHERE sa.shrimp.id = :shrimpId AND sa.attribute.id = :attributeId")
  Optional<ShrimpAttribute> findByShrimpAndAttribute(
          @Param("shrimpId") Integer shrimpId,
          @Param("attributeId") Integer attributeId
  );
}