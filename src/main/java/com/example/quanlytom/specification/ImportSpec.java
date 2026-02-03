package com.example.quanlytom.specification;

import com.example.quanlytom.entity.Import;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class ImportSpec {
    public static Specification<Import> hasSupplier(Integer supplierId) {
        return(root, query, cb) -> {
            if(supplierId == null) {
                return null;
            }
            return cb.equal(root.get("supplier").get("id"), supplierId);
        };
    }

    public static Specification<Import> isBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if(endDate == null) {
                return cb.greaterThanOrEqualTo(root.get("importDate"), startDate);
            }
            if(startDate == null) {
                return cb.lessThanOrEqualTo(root.get("importDate"), endDate);
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
            return cb.between(root.get("importDate"), startDate, endDate);
        };
    }

    public static Specification<Import> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }
}
