package com.example.resortbackendapplication1.resort.booking.repository;

import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortBookingRepository extends JpaRepository<@NonNull ResortBookingEntity, @NonNull Long> {
}
