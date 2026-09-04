package com.example.resortbackendapplication1.payment.repository;

import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PaymentStatusLocaleRepository extends
        JpaRepository<@NonNull PaymentStatusLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PaymentStatusLocaleEntity> {

    Optional<PaymentStatusLocaleEntity> findByPaymentStatusEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long paymentStatusId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPaymentStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long paymentStatusId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull PaymentStatusLocaleEntity> findByPaymentStatusEntity_IdAndIsActiveAndIsDeleted(
            Long paymentStatusId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PaymentStatusLocaleEntity> findByPaymentStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long paymentStatusId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select psle.localeEntity.code from PaymentStatusLocaleEntity psle " +
            "where psle.paymentStatusEntity.id = :paymentStatusId and psle.isActive = :isActive and psle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByPaymentStatusEntity_IdAndIsActiveAndIsDeleted(
            Long paymentStatusId,
            Boolean isActive,
            Boolean isDeleted
    );
}
