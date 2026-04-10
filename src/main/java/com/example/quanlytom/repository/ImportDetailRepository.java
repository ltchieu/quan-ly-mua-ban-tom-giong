package com.example.quanlytom.repository;

import com.example.quanlytom.dto.response.AvailableStockResponse;
import com.example.quanlytom.entity.ImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportDetailRepository extends JpaRepository<ImportDetail, Integer> {

    @Query("""
                select new com.example.quanlytom.dto.response.AvailableStockResponse(
                    ctn.id,
                    t.name,
                    tc.name,
                    cast((ctn.quantity - COALESCE(SUM(ctx.actualQuantity), 0.0d)) as double),
                    ctn.importPrice
                )
                from ImportDetail ctn
            
                left join ExportDetail ctx on ctn.id = ctx.importDetail.id AND ctx.isDeleted = false
                join ShrimpAttribute tct on ctn.shrimpAttribute.id = tct.id
                join Shrimp t on t.id = tct.shrimp.id
                join Attribute tc on tc.id = tct.attribute.id
            
                where ctn.batch.id = :batchId AND ctn.isDeleted = false
                Group by
                	ctn.id,
                	t.name,
                    tc.name,
                	ctn.importPrice,
                    ctn.quantity
            """)
    List<AvailableStockResponse> findAvailableStockByBatchId(@Param("batchId") Integer batchId);
}