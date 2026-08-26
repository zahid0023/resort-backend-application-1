package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortWeeklyScheduleDayEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResortWeeklyScheduleDayRepository extends
        JpaRepository<@NonNull ResortWeeklyScheduleDayEntity, @NonNull Long> {

    List<ResortWeeklyScheduleDayEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted);

    List<ResortWeeklyScheduleDayEntity> findAllByResortEntity_IdAndPriceTypeEntity_CodeAndIsActiveAndIsDeleted(
            Long resortId, String priceTypeCode, Boolean isActive, Boolean isDeleted);
}
