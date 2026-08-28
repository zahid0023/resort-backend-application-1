package com.example.resortbackendapplication1.resort.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.ResortFacilityFilterRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacility.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursDayScheduleRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityoperatinghours.ResortFacilityOperatingHoursWindowRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilities.ResortFacilityResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityOperatingHoursEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resort.facility.model.enums.ResortFacilitySearchField;
import com.example.resortbackendapplication1.resort.facility.model.enums.ResortFacilitySortField;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityOperatingHoursMapper;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityPriceMapper;
import com.example.resortbackendapplication1.resort.facility.repository.ResortFacilityRepository;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityService;
import com.example.resortbackendapplication1.resort.facility.specification.ResortFacilitySpecification;
import com.example.resortbackendapplication1.resort.facility.validation.ResortFacilityOperatingHoursScheduleValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResortFacilityServiceImpl implements ResortFacilityService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortFacilitySortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortFacilitySearchField.allowedFields();

    private final ResortFacilityRepository resortFacilityRepository;

    public ResortFacilityServiceImpl(ResortFacilityRepository resortFacilityRepository) {
        this.resortFacilityRepository = resortFacilityRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityRequest request,
                                  ResortEntity resortEntity,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  FacilityEntity facilityEntity,
                                  LocaleEntity localeEntity,
                                  List<DayOfWeekEntity> allDaysOfWeek,
                                  FacilityPriceTypeEntity facilityPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity) {
        if (resortFacilityRepository.existsByResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortEntity.getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("Resort already has a facility with code: " + request.getCode());
        }

        if (facilityEntity != null && resortFacilityRepository
                .existsByResortEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                        resortEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("Resort already has a facility linked to platform facility id: "
                    + facilityEntity.getId());
        }

        List<ResortFacilityOperatingHoursDayScheduleRequest> operatingHours = request.getOperatingHours();
        boolean hasOperatingHours = operatingHours != null && !operatingHours.isEmpty();
        Map<Long, DayOfWeekEntity> daysOfWeekById = hasOperatingHours
                ? allDaysOfWeek.stream().collect(Collectors.toMap(DayOfWeekEntity::getId, dayOfWeekEntity -> dayOfWeekEntity))
                : Map.of();
        if (hasOperatingHours) {
            ResortFacilityOperatingHoursScheduleValidator.validateWeekCompleteness(operatingHours, daysOfWeekById.keySet());
            operatingHours.forEach(ResortFacilityOperatingHoursScheduleValidator::validateDayShape);
            operatingHours.forEach(day -> ResortFacilityOperatingHoursScheduleValidator.validateWindowsDoNotOverlap(day.getWindows()));
            ResortFacilityOperatingHoursScheduleValidator.validateSpilloverAcrossWeek(operatingHours, allDaysOfWeek);
        }

        ResortFacilityEntity entity = ResortFacilityMapper.create(request, facilityEntity);
        resortEntity.addResortFacilityEntity(entity);
        resortFacilityGroupEntity.addResortFacilityEntity(entity);

        ResortFacilityLocaleEntity resortFacilityLocaleEntity = ResortFacilityLocaleMapper.create(request.getLocale());
        entity.addResortFacilityLocaleEntity(resortFacilityLocaleEntity);
        localeEntity.addResortFacilityLocaleEntity(resortFacilityLocaleEntity);

        if (hasOperatingHours) {
            for (ResortFacilityOperatingHoursDayScheduleRequest day : operatingHours) {
                DayOfWeekEntity dayOfWeekEntity = daysOfWeekById.get(day.getDayOfWeekId());
                if (Boolean.TRUE.equals(day.getIsClosed()) || Boolean.TRUE.equals(day.getIsTwentyFourHours())) {
                    attachOperatingHoursRow(entity, dayOfWeekEntity, day.getIsClosed(), day.getIsTwentyFourHours(), null, null);
                } else {
                    for (ResortFacilityOperatingHoursWindowRequest window : day.getWindows()) {
                        attachOperatingHoursRow(entity, dayOfWeekEntity, false, false, window.getOpensAt(), window.getClosesAt());
                    }
                }
            }
        }

        CreateResortFacilityPriceRequest priceRequest = request.getPrice();
        if (priceRequest != null) {
            ResortFacilityPriceEntity priceEntity = ResortFacilityPriceMapper.create(
                    priceRequest, facilityPriceTypeEntity, priceUnitEntity, currencyEntity);
            entity.addResortFacilityPriceEntity(priceEntity);
        }

        resortFacilityRepository.save(entity);
        log.info("ResortFacility created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void attachOperatingHoursRow(ResortFacilityEntity resortFacilityEntity,
                                         DayOfWeekEntity dayOfWeekEntity,
                                         Boolean isClosed,
                                         Boolean isTwentyFourHours,
                                         LocalTime opensAt,
                                         LocalTime closesAt) {
        ResortFacilityOperatingHoursEntity row = ResortFacilityOperatingHoursMapper.create(
                isClosed, isTwentyFourHours, opensAt, closesAt);
        resortFacilityEntity.addResortFacilityOperatingHoursEntity(row);
        dayOfWeekEntity.addResortFacilityOperatingHoursEntity(row);
    }

    @Override
    public ResortFacilityEntity getEntityById(Long resortId, Long id) {
        return resortFacilityRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacility not found with id: " + id));
    }

    @Override
    public ResortFacilityResponse getById(Long resortId, Long id) {
        ResortFacilityEntity entity = getEntityById(resortId, id);
        ResortFacilityDto dto = ResortFacilityMapper.toDto(entity).build();
        return new ResortFacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortFacilityDto> getAll(Long resortId, ResortFacilityFilterRequest request) {
        Specification<@NonNull ResortFacilityEntity> specification =
                ResortFacilitySpecification.filter(resortId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortFacilitySortField.localeSortFields());
        Page<@NonNull ResortFacilityDto> page = resortFacilityRepository
                .findAll(specification, pageable)
                .map(entity -> ResortFacilityMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityEntity entity,
                                  ResortFacilityGroupEntity resortFacilityGroupEntity,
                                  UpdateResortFacilityRequest request) {
        ResortFacilityGroupEntity currentGroup = entity.getResortFacilityGroupEntity();
        if (!currentGroup.getId().equals(resortFacilityGroupEntity.getId())) {
            currentGroup.removeResortFacilityEntity(entity);
            resortFacilityGroupEntity.addResortFacilityEntity(entity);
        }

        ResortFacilityMapper.update(entity, request);
        resortFacilityRepository.save(entity);
        log.info("ResortFacility updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortFacilityEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortFacilityLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        entity.getResortFacilityOperatingHoursEntities().forEach(operatingHoursEntity -> {
            operatingHoursEntity.setIsDeleted(true);
            operatingHoursEntity.setIsActive(false);
        });

        entity.getResortFacilityPriceEntities().forEach(priceEntity -> {
            priceEntity.setIsDeleted(true);
            priceEntity.setIsActive(false);
        });

        resortFacilityRepository.save(entity);
        log.info("ResortFacility soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
