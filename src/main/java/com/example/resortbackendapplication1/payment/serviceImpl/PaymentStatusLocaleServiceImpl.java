package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.CreatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.UpdatePaymentStatusLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentStatusLocaleMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentStatusLocaleRepository;
import com.example.resortbackendapplication1.payment.service.PaymentStatusLocaleService;
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
public class PaymentStatusLocaleServiceImpl implements PaymentStatusLocaleService {
    private final PaymentStatusLocaleRepository paymentStatusLocaleRepository;

    public PaymentStatusLocaleServiceImpl(PaymentStatusLocaleRepository paymentStatusLocaleRepository) {
        this.paymentStatusLocaleRepository = paymentStatusLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentStatusLocaleRequest request,
                                  PaymentStatusEntity paymentStatusEntity,
                                  LocaleEntity localeEntity) {
        if (paymentStatusLocaleRepository.existsByPaymentStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                paymentStatusEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PaymentStatus already has a locale entry for locale id: " + localeEntity.getId());
        }

        PaymentStatusLocaleEntity entity = PaymentStatusLocaleMapper.create(request);
        paymentStatusEntity.addPaymentStatusLocaleEntity(entity);
        localeEntity.addPaymentStatusLocaleEntity(entity);
        paymentStatusLocaleRepository.save(entity);
        log.info("PaymentStatusLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentStatusLocaleEntity entity,
                                  UpdatePaymentStatusLocaleRequest request) {
        PaymentStatusLocaleMapper.update(entity, request);
        paymentStatusLocaleRepository.save(entity);
        log.info("PaymentStatusLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentStatusLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        paymentStatusLocaleRepository.save(entity);
        log.info("PaymentStatusLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentStatusLocaleEntity getEntityById(Long paymentStatusId, Long id) {
        return paymentStatusLocaleRepository
                .findByPaymentStatusEntity_IdAndIdAndIsActiveAndIsDeleted(paymentStatusId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentStatusLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PaymentStatusLocaleDto> getAll(Long paymentStatusId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PaymentStatusLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? paymentStatusLocaleRepository.findByPaymentStatusEntity_IdAndIsActiveAndIsDeleted(paymentStatusId, true, false, pageable)
                : paymentStatusLocaleRepository.findByPaymentStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        paymentStatusId, localeCode, true, false, pageable))
                .map(PaymentStatusLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long paymentStatusId) {
        List<String> codes = paymentStatusLocaleRepository
                .findLocaleEntity_CodeByPaymentStatusEntity_IdAndIsActiveAndIsDeleted(paymentStatusId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
