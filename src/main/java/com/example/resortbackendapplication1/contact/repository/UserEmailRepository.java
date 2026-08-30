package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface UserEmailRepository extends
        JpaRepository<@NonNull UserEmailEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull UserEmailEntity> {

    Optional<UserEmailEntity> findByUserEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long userId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByEmailAndIsActiveAndIsDeleted(String email, Boolean isActive, Boolean isDeleted);

    boolean existsByEmailAndIsActiveAndIsDeletedAndIdNot(
            String email,
            Boolean isActive,
            Boolean isDeleted,
            Long id
    );

    Page<@NonNull UserEmailEntity> findByUserEntity_IdAndIsActiveAndIsDeleted(
            Long userId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Optional<UserEmailEntity> findByUserEntity_IdAndIsPrimaryAndIsActiveAndIsDeleted(
            Long userId,
            Boolean isPrimary,
            Boolean isActive,
            Boolean isDeleted
    );
}
