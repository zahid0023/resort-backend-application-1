package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.CreatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.locale.UpdatePaymentMethodLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentMethodLocaleMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentMethodLocaleRepository;
import com.example.resortbackendapplication1.payment.service.PaymentMethodLocaleService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PaymentMethodLocaleServiceImpl implements PaymentMethodLocaleService {
    private final PaymentMethodLocaleRepository paymentMethodLocaleRepository;

    public PaymentMethodLocaleServiceImpl(PaymentMethodLocaleRepository paymentMethodLocaleRepository) {
        this.paymentMethodLocaleRepository = paymentMethodLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentMethodLocaleRequest request,
                                  PaymentMethodEntity paymentMethodEntity,
                                  LocaleEntity localeEntity) {
        if (paymentMethodLocaleRepository.existsByPaymentMethodEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                paymentMethodEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PaymentMethod already has a locale entry for locale id: " + localeEntity.getId());
        }

        PaymentMethodLocaleEntity entity = PaymentMethodLocaleMapper.create(request);
        paymentMethodEntity.addPaymentMethodLocaleEntity(entity);
        localeEntity.addPaymentMethodLocaleEntity(entity);
        paymentMethodLocaleRepository.save(entity);
        log.info("PaymentMethodLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentMethodLocaleEntity entity,
                                  UpdatePaymentMethodLocaleRequest request) {
        PaymentMethodLocaleMapper.update(entity, request);
        paymentMethodLocaleRepository.save(entity);
        log.info("PaymentMethodLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentMethodLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        paymentMethodLocaleRepository.save(entity);
        log.info("PaymentMethodLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentMethodLocaleEntity getEntityById(Long paymentMethodId, Long id) {
        return paymentMethodLocaleRepository
                .findByPaymentMethodEntity_IdAndIdAndIsActiveAndIsDeleted(paymentMethodId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentMethodLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PaymentMethodLocaleDto> getAll(Long paymentMethodId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PaymentMethodLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? paymentMethodLocaleRepository.findByPaymentMethodEntity_IdAndIsActiveAndIsDeleted(paymentMethodId, true, false, pageable)
                : paymentMethodLocaleRepository.findByPaymentMethodEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        paymentMethodId, localeCode, true, false, pageable))
                .map(PaymentMethodLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long paymentMethodId) {
        List<String> codes = paymentMethodLocaleRepository
                .findLocaleEntity_CodeByPaymentMethodEntity_IdAndIsActiveAndIsDeleted(paymentMethodId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
