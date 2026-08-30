package com.example.resortbackendapplication1.resort.booking.repository;

import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface BookingGroupRepository extends JpaRepository<@NonNull BookingGroupEntity, @NonNull Long> {

    Optional<BookingGroupEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);
}
