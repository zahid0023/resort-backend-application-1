package com.example.resortbackendapplication1.roomstatus.repository;

import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface RoomStatusLocaleRepository extends
        JpaRepository<@NonNull RoomStatusLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull RoomStatusLocaleEntity> {

    Optional<RoomStatusLocaleEntity> findByRoomStatusEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long roomStatusId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByRoomStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long roomStatusId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull RoomStatusLocaleEntity> findByRoomStatusEntity_IdAndIsActiveAndIsDeleted(
            Long roomStatusId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull RoomStatusLocaleEntity> findByRoomStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long roomStatusId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select rrsle.localeEntity.code from RoomStatusLocaleEntity rrsle " +
            "where rrsle.roomStatusEntity.id = :roomStatusId and rrsle.isActive = :isActive and rrsle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByRoomStatusEntity_IdAndIsActiveAndIsDeleted(
            Long roomStatusId,
            Boolean isActive,
            Boolean isDeleted
    );
}
