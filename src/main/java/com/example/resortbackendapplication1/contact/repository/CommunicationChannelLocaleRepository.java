package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunicationChannelLocaleRepository
        extends JpaRepository<@NonNull CommunicationChannelLocaleEntity, @NonNull Long> {

    Optional<CommunicationChannelLocaleEntity> findByCommunicationChannelEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long channelId, Long id, Boolean isActive, Boolean isDeleted);
}
