package com.example.resortbackendapplication1.auth.repository;

import com.example.resortbackendapplication1.auth.model.entity.PasswordResetOtpEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<@NonNull PasswordResetOtpEntity, @NonNull Long> {

    Optional<PasswordResetOtpEntity> findFirstByUserEntity_IdAndOtpCodeAndUsedAtIsNullAndIsActiveAndIsDeletedOrderByCreatedAtDesc(
            Long userId,
            String otpCode,
            Boolean isActive,
            Boolean isDeleted
    );
}
