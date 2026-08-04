package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface CommunicationChannelLocaleRepository extends
        JpaRepository<@NonNull CommunicationChannelLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull CommunicationChannelLocaleEntity> {

    Optional<CommunicationChannelLocaleEntity> findByCommunicationChannelEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long communicationChannelId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByCommunicationChannelEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long communicationChannelId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull CommunicationChannelLocaleEntity> findByCommunicationChannelEntity_IdAndIsActiveAndIsDeleted(
            Long communicationChannelId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull CommunicationChannelLocaleEntity> findByCommunicationChannelEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long communicationChannelId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
