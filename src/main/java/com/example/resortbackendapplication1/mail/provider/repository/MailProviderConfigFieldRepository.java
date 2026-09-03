package com.example.resortbackendapplication1.mail.provider.repository;

import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigFieldEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface MailProviderConfigFieldRepository extends
        JpaRepository<@NonNull MailProviderConfigFieldEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull MailProviderConfigFieldEntity> {

    Optional<MailProviderConfigFieldEntity> findByMailProviderEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long mailProviderId, Long id, Boolean isActive, Boolean isDeleted);

    List<MailProviderConfigFieldEntity> findByMailProviderEntity_IdAndIsActiveAndIsDeleted(
            Long mailProviderId, Boolean isActive, Boolean isDeleted);

    boolean existsByMailProviderEntity_IdAndKeyAndIsActiveAndIsDeleted(
            Long mailProviderId, String key, Boolean isActive, Boolean isDeleted);
}
