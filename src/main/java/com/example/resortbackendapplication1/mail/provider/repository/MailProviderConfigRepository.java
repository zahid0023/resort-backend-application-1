package com.example.resortbackendapplication1.mail.provider.repository;

import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface MailProviderConfigRepository extends
        JpaRepository<@NonNull MailProviderConfigEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull MailProviderConfigEntity> {

    Optional<MailProviderConfigEntity> findByMailProviderEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long mailProviderId, Long id, Boolean isActive, Boolean isDeleted);

    Optional<MailProviderConfigEntity> findByIdAndIsActiveAndIsDeleted(
            Long id, Boolean isActive, Boolean isDeleted);

    Optional<MailProviderConfigEntity> findByCodeAndIsActiveAndIsDeleted(
            MailProviderConfigCode code, Boolean isActive, Boolean isDeleted);

    boolean existsByMailProviderEntity_IdAndNameAndIsActiveAndIsDeleted(
            Long mailProviderId, String name, Boolean isActive, Boolean isDeleted);

    boolean existsByMailProviderEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
            Long mailProviderId, String name, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(
            MailProviderConfigCode code, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIdNotAndIsActiveAndIsDeleted(
            MailProviderConfigCode code, Long id, Boolean isActive, Boolean isDeleted);
}
