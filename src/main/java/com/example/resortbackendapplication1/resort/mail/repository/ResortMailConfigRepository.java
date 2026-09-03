package com.example.resortbackendapplication1.resort.mail.repository;

import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortMailConfigRepository extends
        JpaRepository<@NonNull ResortMailConfigEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortMailConfigEntity> {

    Optional<ResortMailConfigEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndNameAndIsActiveAndIsDeleted(
            Long resortId, String name, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
            Long resortId, String name, Long id, Boolean isActive, Boolean isDeleted);
}
