package com.example.resortbackendapplication1.roomcategory.repository;

import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface RoomCategoryLocaleRepository extends
        JpaRepository<@NonNull RoomCategoryLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull RoomCategoryLocaleEntity> {

    Optional<RoomCategoryLocaleEntity> findByRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long roomCategoryId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByRoomCategoryEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long roomCategoryId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
            Long localeId,
            String name,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull RoomCategoryLocaleEntity> findByRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long roomCategoryId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull RoomCategoryLocaleEntity> findByRoomCategoryEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long roomCategoryId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
