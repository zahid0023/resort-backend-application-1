package com.example.resortbackendapplication1.resort.booking.repository;

import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("unused")
public interface ResortBookingRepository extends JpaRepository<@NonNull ResortBookingEntity, @NonNull Long> {

    /** Backs the reference_code generated in ResortBookingServiceImpl#create — see resort_booking_reference_code_seq (V45). */
    @Query(value = "select nextval('resort_booking_reference_code_seq')", nativeQuery = true)
    Long nextReferenceCodeSequenceValue();
}
