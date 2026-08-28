package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomLocaleRepository extends JpaRepository<@NonNull ResortRoomLocaleEntity, @NonNull Long> {

    Optional<ResortRoomLocaleEntity> findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortRoomEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long localeId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortRoomLocaleEntity> findByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull ResortRoomLocaleEntity> findByResortRoomEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long resortRoomId, String localeCode, Boolean isActive, Boolean isDeleted, Pageable pageable);

    /**
     * The platform {@code Locale} codes this resort room already has an active translation for — matched via
     * {@code locale_id}, mirroring {@code ResortRoomCategoryLocaleRepository}'s equivalent count query.
     */
    @Query("select rrl.localeEntity.code from ResortRoomLocaleEntity rrl "
            + "where rrl.resortRoomEntity.id = :resortRoomId "
            + "and rrl.isActive = :isActive and rrl.isDeleted = :isDeleted")
    List<String> findLocaleCodeByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted);
}
