package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.PaymentProviderFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.CreatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.UpdatePaymentProviderRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentproviders.PaymentProviderResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodDto;
import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentProviderLocaleEntity;
import com.example.resortbackendapplication1.payment.model.enums.PaymentProviderSearchField;
import com.example.resortbackendapplication1.payment.model.enums.PaymentProviderSortField;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentMethodMapper;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentProviderLocaleMapper;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentProviderMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentProviderRepository;
import com.example.resortbackendapplication1.payment.service.PaymentProviderService;
import com.example.resortbackendapplication1.payment.specification.PaymentProviderSpecification;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PaymentProviderSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PaymentProviderSearchField.allowedFields();

    private final PaymentProviderRepository paymentProviderRepository;

    public PaymentProviderServiceImpl(PaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderRepository = paymentProviderRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentProviderRequest request, PaymentMethodEntity paymentMethodEntity, LocaleEntity localeEntity) {
        if (paymentProviderRepository.existsByPaymentMethodEntity_IdAndCodeAndIsActiveAndIsDeleted(
                paymentMethodEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("PaymentProvider with code '" + request.getCode() + "' already exists for this payment method");
        }

        PaymentProviderEntity entity = PaymentProviderMapper.create(request);
        paymentMethodEntity.addPaymentProviderEntity(entity);

        PaymentProviderLocaleEntity paymentProviderLocaleEntity = PaymentProviderLocaleMapper.create(request.getLocale());
        localeEntity.addPaymentProviderLocaleEntity(paymentProviderLocaleEntity);
        entity.addPaymentProviderLocaleEntity(paymentProviderLocaleEntity);

        paymentProviderRepository.save(entity);
        log.info("PaymentProvider created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentProviderEntity getEntityById(Long id) {
        return paymentProviderRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentProvider not found with id: " + id));
    }

    @Override
    public PaymentProviderResponse getById(Long id) {
        PaymentProviderEntity entity = getEntityById(id);
        PaymentMethodDto paymentMethod = PaymentMethodMapper.toDto(entity.getPaymentMethodEntity()).build();
        PaymentProviderDto dto = PaymentProviderMapper.toDto(entity)
                .paymentMethod(paymentMethod)
                .build();
        return new PaymentProviderResponse(dto);
    }

    @Override
    public PaginatedResponse<PaymentProviderDto> getAll(PaymentProviderFilterRequest request) {
        Specification<@NonNull PaymentProviderEntity> specification =
                PaymentProviderSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PaymentProviderSortField.localeSortFields());
        Page<@NonNull PaymentProviderDto> page = paymentProviderRepository
                .findAll(specification, pageable)
                .map(entity -> PaymentProviderMapper.toDto(entity)
                        .paymentMethod(PaymentMethodMapper.toDto(entity.getPaymentMethodEntity()).build())
                        .build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentProviderEntity entity, UpdatePaymentProviderRequest request) {
        PaymentProviderMapper.update(entity, request);
        paymentProviderRepository.save(entity);
        log.info("PaymentProvider updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentProviderEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getPaymentProviderLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        paymentProviderRepository.save(entity);
        log.info("PaymentProvider soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
