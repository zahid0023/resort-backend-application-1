package com.example.resortbackendapplication1.payment.repository;

import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PaymentProviderLocaleRepository extends
        JpaRepository<@NonNull PaymentProviderLocaleEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PaymentProviderLocaleEntity> {

    Optional<PaymentProviderLocaleEntity> findByPaymentProviderEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long paymentProviderId,
            Long id,
            Boolean isActive,
            Boolean isDeleted
    );

    boolean existsByPaymentProviderEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
            Long paymentProviderId,
            Long localeId,
            Boolean isActive,
            Boolean isDeleted
    );

    Page<@NonNull PaymentProviderLocaleEntity> findByPaymentProviderEntity_IdAndIsActiveAndIsDeleted(
            Long paymentProviderId,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    Page<@NonNull PaymentProviderLocaleEntity> findByPaymentProviderEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
            Long paymentProviderId,
            String localeCode,
            Boolean isActive,
            Boolean isDeleted,
            Pageable pageable
    );

    @Query("select ple.localeEntity.code from PaymentProviderLocaleEntity ple " +
            "where ple.paymentProviderEntity.id = :paymentProviderId and ple.isActive = :isActive and ple.isDeleted = :isDeleted")
    List<String> findLocaleEntity_CodeByPaymentProviderEntity_IdAndIsActiveAndIsDeleted(
            Long paymentProviderId,
            Boolean isActive,
            Boolean isDeleted
    );
}
