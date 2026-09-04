package com.example.resortbackendapplication1.payment.repository;

import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PaymentMethodLocaleRepository extends
        JpaRepository<@NonNull PaymentMethodLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PaymentMethodLocaleEntity> {

    Optional<PaymentMethodLocaleEntity> findByPaymentMethodEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPaymentMethodEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull PaymentMethodLocaleEntity> findByPaymentMethodEntity_IdAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PaymentMethodLocaleEntity> findByPaymentMethodEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select pmle.localeEntity.code from PaymentMethodLocaleEntity pmle " +
            "where pmle.paymentMethodEntity.id = :paymentMethodId and pmle.isActive = :isActive and pmle.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByPaymentMethodEntity_IdAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            Boolean isActive,
            Boolean isDeleted
    );
}
