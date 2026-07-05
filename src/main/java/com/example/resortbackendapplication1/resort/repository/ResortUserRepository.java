package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortUserRepository extends JpaRepository<@NonNull ResortUserEntity, @NonNull Long> {

    boolean existsByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long userId, Boolean isActive, Boolean isDeleted);
}
