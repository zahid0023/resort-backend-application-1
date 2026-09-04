package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.CreatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.UpdatePaymentProviderLocaleRequest;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderLocaleDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentProviderLocaleMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentProviderLocaleRepository;
import com.example.resortbackendapplication1.payment.service.PaymentProviderLocaleService;
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
public class PaymentProviderLocaleServiceImpl implements PaymentProviderLocaleService {
    private final PaymentProviderLocaleRepository paymentProviderLocaleRepository;

    public PaymentProviderLocaleServiceImpl(PaymentProviderLocaleRepository paymentProviderLocaleRepository) {
        this.paymentProviderLocaleRepository = paymentProviderLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentProviderLocaleRequest request,
                                  PaymentProviderEntity paymentProviderEntity,
                                  LocaleEntity localeEntity) {
        if (paymentProviderLocaleRepository.existsByPaymentProviderEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                paymentProviderEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("PaymentProvider already has a locale entry for locale id: " + localeEntity.getId());
        }

        PaymentProviderLocaleEntity entity = PaymentProviderLocaleMapper.create(request);
        paymentProviderEntity.addPaymentProviderLocaleEntity(entity);
        localeEntity.addPaymentProviderLocaleEntity(entity);
        paymentProviderLocaleRepository.save(entity);
        log.info("PaymentProviderLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentProviderLocaleEntity entity,
                                  UpdatePaymentProviderLocaleRequest request) {
        PaymentProviderLocaleMapper.update(entity, request);
        paymentProviderLocaleRepository.save(entity);
        log.info("PaymentProviderLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentProviderLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        paymentProviderLocaleRepository.save(entity);
        log.info("PaymentProviderLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentProviderLocaleEntity getEntityById(Long paymentProviderId, Long id) {
        return paymentProviderLocaleRepository
                .findByPaymentProviderEntity_IdAndIdAndIsActiveAndIsDeleted(paymentProviderId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentProviderLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PaymentProviderLocaleDto> getAll(Long paymentProviderId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PaymentProviderLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? paymentProviderLocaleRepository.findByPaymentProviderEntity_IdAndIsActiveAndIsDeleted(paymentProviderId, true, false, pageable)
                : paymentProviderLocaleRepository.findByPaymentProviderEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        paymentProviderId, localeCode, true, false, pageable))
                .map(PaymentProviderLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long paymentProviderId) {
        List<String> codes = paymentProviderLocaleRepository
                .findLocaleEntity_CodeByPaymentProviderEntity_IdAndIsActiveAndIsDeleted(paymentProviderId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
