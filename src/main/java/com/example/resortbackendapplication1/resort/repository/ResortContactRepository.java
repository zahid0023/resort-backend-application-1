package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortContactEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortContactRepository extends
        JpaRepository<@NonNull ResortContactEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortContactEntity> {

    Optional<ResortContactEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndContactTypeEntity_IdAndCommunicationChannelEntity_IdAndContactValueAndIsActiveAndIsDeleted(
            Long resortId, Long contactTypeId, Long communicationChannelId, String contactValue,
            Boolean isActive, Boolean isDeleted);

    Optional<ResortContactEntity> findByResortEntity_IdAndContactTypeEntity_IdAndCommunicationChannelEntity_IdAndIsPrimaryTrueAndIsActiveAndIsDeleted(
            Long resortId, Long contactTypeId, Long communicationChannelId,
            Boolean isActive, Boolean isDeleted);
}
