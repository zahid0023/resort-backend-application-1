package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomFacilityLocaleRepository extends JpaRepository<@NonNull ResortRoomFacilityLocaleEntity, @NonNull Long> {

    Optional<ResortRoomFacilityLocaleEntity> findByResortRoomFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomFacilityLocaleEntity> findByResortRoomFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomFacilityLocaleEntity> findByResortRoomFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomFacilityId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rrfl.localeEntity.code from ResortRoomFacilityLocaleEntity rrfl "
            + "where rrfl.resortRoomFacilityEntity.id = :resortRoomFacilityId "
            + "and rrfl.isActive = :isActive and rrfl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoomFacilityEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityId, Boolean isActive, Boolean isDeleted);
}
