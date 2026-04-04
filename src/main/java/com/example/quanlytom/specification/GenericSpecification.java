package com.example.quanlytom.specification;

import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class GenericSpecification {

    public static <T> Specification<T> hasJoinAttribute(String attributeName, Integer id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }
            return cb.equal(root.get(attributeName).get("id"), id);
        };
    }

    public static <T> Specification<T> isBetweenDates(LocalDateTime startDate, LocalDateTime endDate, String dateFieldName) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (endDate == null) {
                return cb.greaterThanOrEqualTo(root.get(dateFieldName), startDate);
            }
            if (startDate == null) {
                return cb.lessThanOrEqualTo(root.get(dateFieldName), endDate);
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
            return cb.between(root.get(dateFieldName), startDate, endDate);
        };
    }

    public static <T> Specification<T> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }
}
