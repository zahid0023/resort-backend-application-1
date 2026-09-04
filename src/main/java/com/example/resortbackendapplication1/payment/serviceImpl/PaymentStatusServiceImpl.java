package com.example.resortbackendapplication1.payment.serviceImpl;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.PaymentStatusFilterRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.CreatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.UpdatePaymentStatusRequest;
import com.example.resortbackendapplication1.payment.dto.response.paymentstatuses.PaymentStatusResponse;
import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusDto;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusEntity;
import com.example.resortbackendapplication1.payment.model.entity.PaymentStatusLocaleEntity;
import com.example.resortbackendapplication1.payment.model.enums.PaymentStatusSearchField;
import com.example.resortbackendapplication1.payment.model.enums.PaymentStatusSortField;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentStatusLocaleMapper;
import com.example.resortbackendapplication1.payment.model.mapper.PaymentStatusMapper;
import com.example.resortbackendapplication1.payment.repository.PaymentStatusRepository;
import com.example.resortbackendapplication1.payment.service.PaymentStatusService;
import com.example.resortbackendapplication1.payment.specification.PaymentStatusSpecification;
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
public class PaymentStatusServiceImpl implements PaymentStatusService {

    private static final Set<String> ALLOWED_SORT_FIELDS = PaymentStatusSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = PaymentStatusSearchField.allowedFields();

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreatePaymentStatusRequest request, LocaleEntity localeEntity) {
        if (paymentStatusRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("PaymentStatus with code '" + request.getCode() + "' already exists");
        }

        PaymentStatusEntity entity = PaymentStatusMapper.create(request);

        PaymentStatusLocaleEntity paymentStatusLocaleEntity = PaymentStatusLocaleMapper.create(request.getLocale());
        localeEntity.addPaymentStatusLocaleEntity(paymentStatusLocaleEntity);

        entity.addPaymentStatusLocaleEntity(paymentStatusLocaleEntity);

        paymentStatusRepository.save(entity);
        log.info("PaymentStatus created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaymentStatusEntity getEntityById(Long id) {
        return paymentStatusRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PaymentStatus not found with id: " + id));
    }

    @Override
    public PaymentStatusResponse getById(Long id) {
        PaymentStatusEntity entity = getEntityById(id);
        PaymentStatusDto dto = PaymentStatusMapper.toDto(entity).build();
        return new PaymentStatusResponse(dto);
    }

    @Override
    public PaginatedResponse<PaymentStatusDto> getAll(PaymentStatusFilterRequest request) {
        Specification<@NonNull PaymentStatusEntity> specification =
                PaymentStatusSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, PaymentStatusSortField.localeSortFields());
        Page<@NonNull PaymentStatusDto> page = paymentStatusRepository
                .findAll(specification, pageable)
                .map(entity -> PaymentStatusMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(PaymentStatusEntity entity, UpdatePaymentStatusRequest request) {
        PaymentStatusMapper.update(entity, request);
        paymentStatusRepository.save(entity);
        log.info("PaymentStatus updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PaymentStatusEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getPaymentStatusLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        paymentStatusRepository.save(entity);
        log.info("PaymentStatus soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
