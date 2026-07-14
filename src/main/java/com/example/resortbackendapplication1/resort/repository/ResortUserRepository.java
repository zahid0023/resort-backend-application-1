package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResortUserRepository extends JpaRepository<@NonNull ResortUserEntity, @NonNull Long> {

    boolean existsByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long userId, Boolean isActive, Boolean isDeleted);

    Optional<ResortUserEntity> findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortId, Boolean isActive, Boolean isDeleted);

    Optional<ResortUserEntity> findByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(Long resortId, Long userId, Boolean isActive, Boolean isDeleted);

    Page<@NonNull ResortUserEntity> findAllByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
