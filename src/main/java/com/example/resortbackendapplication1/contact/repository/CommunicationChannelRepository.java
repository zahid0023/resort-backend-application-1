package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommunicationChannelRepository
        extends JpaRepository<@NonNull CommunicationChannelEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull CommunicationChannelEntity> {

    Optional<CommunicationChannelEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<CommunicationChannelEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    Optional<CommunicationChannelEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
