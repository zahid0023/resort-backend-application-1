package com.example.resortbackendapplication1.payment.repository;

import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PaymentStatusRepository extends
        JpaRepository<@NonNull PaymentStatusEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PaymentStatusEntity> {

    Optional<PaymentStatusEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
