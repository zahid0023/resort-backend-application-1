package com.example.resortbackendapplication1.mail.provider.repository;

import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface MailProviderRepository extends
        JpaRepository<@NonNull MailProviderEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull MailProviderEntity> {

    Optional<MailProviderEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
