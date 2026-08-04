package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ContactTypeLocaleRepository extends
        JpaRepository<@NonNull ContactTypeLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ContactTypeLocaleEntity> {

    Optional<ContactTypeLocaleEntity> findByContactTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long contactTypeId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByContactTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long contactTypeId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull ContactTypeLocaleEntity> findByContactTypeEntity_IdAndIsActiveAndIsDeleted(
            Long contactTypeId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull ContactTypeLocaleEntity> findByContactTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long contactTypeId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );
}
