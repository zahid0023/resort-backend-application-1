package com.example.resortbackendapplication1.resort.booking.repository;

import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortBookingRepository extends JpaRepository<@NonNull ResortBookingEntity, @NonNull Long> {

    Page<@NonNull ResortBookingEntity> findByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Optional<ResortBookingEntity> findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortId, Boolean isActive, Boolean isDeleted);
}
