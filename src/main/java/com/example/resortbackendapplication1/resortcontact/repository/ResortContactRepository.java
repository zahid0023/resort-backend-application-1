package com.example.resortbackendapplication1.resortcontact.repository;

import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ResortContactRepository extends JpaRepository<@NonNull ResortContactEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortContactEntity> {

    Optional<ResortContactEntity> findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(
            Long id, Long resortId, Boolean isActive, Boolean isDeleted);
}
