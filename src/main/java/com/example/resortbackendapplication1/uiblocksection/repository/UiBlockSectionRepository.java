package com.example.resortbackendapplication1.uiblocksection.repository;

import com.example.resortbackendapplication1.uiblocksection.model.entity.UiBlockSectionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UiBlockSectionRepository extends JpaRepository<@NonNull UiBlockSectionEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull UiBlockSectionEntity> {

    Optional<UiBlockSectionEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<UiBlockSectionEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
