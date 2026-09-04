package com.example.resortbackendapplication1.payment.repository;

import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PaymentProviderRepository extends
        JpaRepository<@NonNull PaymentProviderEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PaymentProviderEntity> {

    Optional<PaymentProviderEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByPaymentMethodEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long paymentMethodId,
            String code,
            Boolean isActive,
            Boolean isDeleted
    );

}
