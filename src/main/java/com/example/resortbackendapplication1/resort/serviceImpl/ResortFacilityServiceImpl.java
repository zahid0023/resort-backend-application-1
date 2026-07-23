package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facilitypricetype.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.SetResortFacilityHighlightsRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortFacilitySortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.repository.ResortFacilityRepository;
import com.example.resortbackendapplication1.resort.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.specification.ResortFacilitySpecification;
import com.example.resortbackendapplication1.resortfacilityprice.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resortfacilityprice.model.mapper.ResortFacilityPriceMapper;
import com.example.resortbackendapplication1.resortfacilityprice.repository.ResortFacilityPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ResortFacilityServiceImpl implements ResortFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private static final String PAID_CODE = "PAID";

    private final ResortFacilityRepository resortFacilityRepository;
    private final ResortFacilityPriceRepository resortFacilityPriceRepository;

    public ResortFacilityServiceImpl(ResortFacilityRepository resortFacilityRepository,
                                     ResortFacilityPriceRepository resortFacilityPriceRepository) {
        this.resortFacilityRepository = resortFacilityRepository;
        this.resortFacilityPriceRepository = resortFacilityPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityRequest request,
                                  ResortEntity resortEntity,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  FacilityEntity facilityEntity,
                                  FacilityPriceTypeEntity facilityPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity,
                                  Map<Long, LocaleEntity> localeEntityMap) {
        boolean isPaid = facilityPriceTypeEntity != null && PAID_CODE.equals(facilityPriceTypeEntity.getCode());
        if (isPaid && request.getResortFacilityPrice() == null) {
            throw new IllegalArgumentException("A price is required when facility price type is PAID.");
        }
        if (!isPaid && request.getResortFacilityPrice() != null) {
            throw new IllegalArgumentException("A price can only be provided when facility price type is PAID.");
        }
        ResortFacilityEntity entity = ResortFacilityMapper.create(request, resortEntity, resortFacilityGroupEntity, facilityEntity, facilityPriceTypeEntity, localeEntityMap);
        resortFacilityRepository.save(entity);
        if (isPaid) {
            ResortFacilityPriceEntity priceEntity = ResortFacilityPriceMapper.create(
                    request.getResortFacilityPrice(), entity, priceUnitEntity, currencyEntity);
            resortFacilityPriceRepository.save(priceEntity);
            log.info("ResortFacilityPrice created with id: {} for ResortFacility id: {}", priceEntity.getId(), entity.getId());
        }
        log.info("ResortFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityEntity getEntityById(Long id) {
        return resortFacilityRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacility not found with id: " + id));
    }

    @Override
    public ResortFacilityResponse getById(Long id, Long resortId) {
        ResortFacilityEntity entity = resortFacilityRepository
                .findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(id, resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacility not found with id: " + id));
        return new ResortFacilityResponse(ResortFacilityMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortFacilityDto> getAll(ResortFacilityFilterRequest request, Long resortFacilityGroupId) {
        Page<@NonNull ResortFacilityDto> page = resortFacilityRepository
                .findAll(ResortFacilitySpecification.filter(request, resortFacilityGroupId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortFacilityMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityEntity entity,
                                  UpdateResortFacilityRequest request,
                                  FacilityEntity facilityEntity,
                                  FacilityPriceTypeEntity facilityPriceTypeEntity) {
        ResortFacilityMapper.update(entity, request, facilityEntity, facilityPriceTypeEntity);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortFacilityEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ResortFacilityEntity> getAll(Set<Long> ids) {
        List<ResortFacilityEntity> entities = resortFacilityRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ResortFacilityEntity::getId, "ResortFacility");
        return entities;
    }

    @Transactional
    @Override
    public SuccessResponse setHighlights(Long resortId, SetResortFacilityHighlightsRequest request) {
        Set<Long> requestedIds = request.getFacilityIds();

        List<ResortFacilityEntity> allFacilities = resortFacilityRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false);

        Set<Long> allFacilityIds = allFacilities.stream()
                .map(ResortFacilityEntity::getId)
                .collect(java.util.stream.Collectors.toSet());

        Set<Long> invalidIds = requestedIds.stream()
                .filter(id -> !allFacilityIds.contains(id))
                .collect(java.util.stream.Collectors.toSet());

        if (!invalidIds.isEmpty()) {
            throw new ValidationException("The following facility IDs do not belong to resort " + resortId + ": " + invalidIds);
        }

        for (ResortFacilityEntity facility : allFacilities) {
            facility.setIsHighlighted(requestedIds.contains(facility.getId()));
        }

        resortFacilityRepository.saveAll(allFacilities);
        log.info("Resort {} highlights updated: {} facilities highlighted", resortId, requestedIds.size());
        return new SuccessResponse(true, resortId);
    }
}
