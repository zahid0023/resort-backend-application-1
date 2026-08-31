package com.example.resortbackendapplication1.bookingsource.repository;

import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface BookingSourceRepository extends
        JpaRepository<@NonNull BookingSourceEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull BookingSourceEntity> {

    Optional<BookingSourceEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
