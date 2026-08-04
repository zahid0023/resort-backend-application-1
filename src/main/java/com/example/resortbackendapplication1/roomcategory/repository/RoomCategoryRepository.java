package com.example.resortbackendapplication1.roomcategory.repository;

import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface RoomCategoryRepository extends
        JpaRepository<@NonNull RoomCategoryEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull RoomCategoryEntity> {

    Optional<RoomCategoryEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
