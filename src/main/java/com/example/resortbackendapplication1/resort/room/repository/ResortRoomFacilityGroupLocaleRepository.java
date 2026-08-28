package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomFacilityGroupLocaleRepository extends JpaRepository<@NonNull ResortRoomFacilityGroupLocaleEntity, @NonNull Long> {

    Optional<ResortRoomFacilityGroupLocaleEntity> findByResortRoomFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityGroupId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityGroupId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomFacilityGroupLocaleEntity> findByResortRoomFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityGroupId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomFacilityGroupLocaleEntity> findByResortRoomFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomFacilityGroupId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    @Query("select rrfgl.localeEntity.code from ResortRoomFacilityGroupLocaleEntity rrfgl "
            + "where rrfgl.resortRoomFacilityGroupEntity.id = :resortRoomFacilityGroupId "
            + "and rrfgl.isActive = :isActive and rrfgl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoomFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomFacilityGroupId, Boolean isActive, Boolean isDeleted);
}
