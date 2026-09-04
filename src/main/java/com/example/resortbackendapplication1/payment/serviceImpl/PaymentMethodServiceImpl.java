package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.PaymentMethodFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.CreatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentmethod.UpdatePaymentMethodRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentmethods.PaymentMethodResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentMethodLocaleEntity;
import com.example.resortbackendapplication1.payment.model.enums.PaymentMethodSearchField;
import com.example.resortbackendapplication1.payment.model.enums.PaymentMethodSortField;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentMethodLocaleMapper;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentMethodMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentMethodRepository;
import com.example.resortbackendapplication1.payment.service.PaymentMethodService;
import com.example.resortbackendapplication1.payment.specification.PaymentMethodSpecification;
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
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PaymentMethodSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PaymentMethodSearchField.allowedFields();

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentMethodRequest request, LocaleEntity localeEntity) {
        if (paymentMethodRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("PaymentMethod with code '" + request.getCode() + "' already exists");
        }

        PaymentMethodEntity entity = PaymentMethodMapper.create(request);

        PaymentMethodLocaleEntity paymentMethodLocaleEntity = PaymentMethodLocaleMapper.create(request.getLocale());
        localeEntity.addPaymentMethodLocaleEntity(paymentMethodLocaleEntity);

        entity.addPaymentMethodLocaleEntity(paymentMethodLocaleEntity);

        paymentMethodRepository.save(entity);
        log.info("PaymentMethod created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentMethodEntity getEntityById(Long id) {
        return paymentMethodRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentMethod not found with id: " + id));
    }

    @Override
    public PaymentMethodResponse getById(Long id) {
        PaymentMethodEntity entity = getEntityById(id);
        PaymentMethodDto dto = PaymentMethodMapper.toDto(entity).build();
        return new PaymentMethodResponse(dto);
    }

    @Override
    public PaginatedResponse<PaymentMethodDto> getAll(PaymentMethodFilterRequest request) {
        Specification<@NonNull PaymentMethodEntity> specification =
                PaymentMethodSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PaymentMethodSortField.localeSortFields());
        Page<@NonNull PaymentMethodDto> page = paymentMethodRepository
                .findAll(specification, pageable)
                .map(entity -> PaymentMethodMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentMethodEntity entity, UpdatePaymentMethodRequest request) {
        PaymentMethodMapper.update(entity, request);
        paymentMethodRepository.save(entity);
        log.info("PaymentMethod updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentMethodEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getPaymentMethodLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        paymentMethodRepository.save(entity);
        log.info("PaymentMethod soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
