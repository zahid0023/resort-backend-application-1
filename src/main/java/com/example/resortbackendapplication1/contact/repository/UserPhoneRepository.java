package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.UserPhoneEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface UserPhoneRepository extends
        JpaRepository<@NonNull UserPhoneEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull UserPhoneEntity> {

    Optional<UserPhoneEntity> findByUserEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long userId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPhoneAndIsActiveAndIsDeleted(String phone, Boolean isActive, Boolean isDeleted);

    boolean existsByPhoneAndIsActiveAndIsDeletedAndIdNot(
            String phone,
            Boolean isActive,
            Boolean isDeleted,
            Long id
    );

    Page<@NonNull UserPhoneEntity> findByUserEntity_IdAndIsActiveAndIsDeleted(
            Long userId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Optional<UserPhoneEntity> findByUserEntity_IdAndIsPrimaryAndIsActiveAndIsDeleted(
            Long userId,
            Boolean isPrimary,
            Boolean isActive,
            Boolean isDeleted
    );
}
