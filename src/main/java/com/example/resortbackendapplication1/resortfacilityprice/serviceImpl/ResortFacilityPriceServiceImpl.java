package com.example.resortbackendapplication1.resortfacilityprice.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.ResortFacilityPriceFilterRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.request.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resortfacilityprice.dto.response.ResortFacilityPriceResponse;
import com.example.resortbackendapplication1.resortfacilityprice.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resortfacilityprice.model.enums.ResortFacilityPriceSortField;
import com.example.resortbackendapplication1.resortfacilityprice.model.mapper.ResortFacilityPriceMapper;
import com.example.resortbackendapplication1.resortfacilityprice.repository.ResortFacilityPriceRepository;
import com.example.resortbackendapplication1.resortfacilityprice.service.ResortFacilityPriceService;
import com.example.resortbackendapplication1.resortfacilityprice.specification.ResortFacilityPriceSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortFacilityPriceServiceImpl implements ResortFacilityPriceService {

    private static final String PAID_CODE = "PAID";
    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilityPriceSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ResortFacilityPriceRepository resortFacilityPriceRepository;

    public ResortFacilityPriceServiceImpl(ResortFacilityPriceRepository resortFacilityPriceRepository) {
        this.resortFacilityPriceRepository = resortFacilityPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityPriceRequest request,
                                  ResortFacilityEntity resortFacilityEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity) {
        validatePaidFacility(resortFacilityEntity);
        ResortFacilityPriceEntity entity = ResortFacilityPriceMapper.create(
                request, resortFacilityEntity, priceUnitEntity, currencyEntity);
        resortFacilityPriceRepository.save(entity);
        log.info("ResortFacilityPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public ResortFacilityPriceEntity getEntityById(Long resortFacilityId, Long id) {
        return resortFacilityPriceRepository
                .findByIdAndResortFacilityEntity_IdAndIsActiveAndIsDeleted(id, resortFacilityId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityPrice not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public ResortFacilityPriceResponse getById(Long resortFacilityId, Long id) {
        return new ResortFacilityPriceResponse(
                ResortFacilityPriceMapper.toDto(getEntityById(resortFacilityId, id)));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<ResortFacilityPriceDto> getAll(ResortFacilityPriceFilterRequest request,
                                                             Long resortFacilityId) {
        Page<@NonNull ResortFacilityPriceEntity> entityPage = resortFacilityPriceRepository
                .findAll(ResortFacilityPriceSpecification.filter(request, resortFacilityId),
                        request.toPageable(ALLOWED_SORT_FIELDS));
        Page<@NonNull ResortFacilityPriceDto> page = entityPage.map(ResortFacilityPriceMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityPriceEntity entity,
                                  UpdateResortFacilityPriceRequest request,
                                  PriceUnitEntity priceUnitEntity) {
        ResortFacilityPriceMapper.update(entity, request, priceUnitEntity);
        resortFacilityPriceRepository.save(entity);
        log.info("ResortFacilityPrice updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityPriceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityPriceRepository.save(entity);
        log.info("ResortFacilityPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void validatePaidFacility(ResortFacilityEntity resortFacilityEntity) {
        String code = resortFacilityEntity.getFacilityPriceTypeEntity().getCode();
        if (!PAID_CODE.equals(code)) {
            throw new IllegalStateException(
                    "Prices can only be added to facilities with price type PAID. Current price type: " + code);
        }
    }
}
