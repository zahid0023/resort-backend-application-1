package com.example.resortbackendapplication1.bookingsource.repository;

import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface BookingSourceLocaleRepository extends
        JpaRepository<@NonNull BookingSourceLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull BookingSourceLocaleEntity> {

    Optional<BookingSourceLocaleEntity> findByBookingSourceEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long bookingSourceId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByBookingSourceEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long bookingSourceId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull BookingSourceLocaleEntity> findByBookingSourceEntity_IdAndIsActiveAndIsDeleted(
            Long bookingSourceId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull BookingSourceLocaleEntity> findByBookingSourceEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long bookingSourceId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select rsl.localeEntity.code from BookingSourceLocaleEntity rsl " +
            "where rsl.bookingSourceEntity.id = :bookingSourceId and rsl.isActive = :isActive and rsl.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByBookingSourceEntity_IdAndIsActiveAndIsDeleted(
            Long bookingSourceId,
            Boolean isActive,
            Boolean isDeleted
    );
}
