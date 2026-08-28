package com.example.resortbackendapplication1.resort.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.FacilityPriceTypeEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.price.model.mapper.FacilityPriceTypeMapper;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.CreateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.request.resortfacilityprice.UpdateResortFacilityPriceRequest;
import com.example.resortbackendapplication1.resort.facility.dto.response.resortfacilityprices.ResortFacilityPriceResponse;
import com.example.resortbackendapplication1.resort.facility.model.dto.ResortFacilityPriceDto;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.resort.facility.model.entity.ResortFacilityPriceEntity;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.resort.facility.model.mapper.ResortFacilityPriceMapper;
import com.example.resortbackendapplication1.resort.facility.repository.ResortFacilityPriceRepository;
import com.example.resortbackendapplication1.resort.facility.service.ResortFacilityPriceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortFacilityPriceServiceImpl implements ResortFacilityPriceService {

    private final ResortFacilityPriceRepository resortFacilityPriceRepository;

    public ResortFacilityPriceServiceImpl(ResortFacilityPriceRepository resortFacilityPriceRepository) {
        this.resortFacilityPriceRepository = resortFacilityPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortFacilityPriceRequest request,
                                  ResortFacilityEntity resortFacilityEntity,
                                  FacilityPriceTypeEntity facilityPriceTypeEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  CurrencyEntity currencyEntity) {
        ResortFacilityPriceEntity entity = ResortFacilityPriceMapper.create(
                request, facilityPriceTypeEntity, priceUnitEntity, currencyEntity);
        resortFacilityEntity.addResortFacilityPriceEntity(entity);

        resortFacilityPriceRepository.save(entity);
        log.info("ResortFacilityPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityPriceEntity getEntityById(Long resortFacilityId, Long id) {
        return resortFacilityPriceRepository
                .findByResortFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(resortFacilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortFacilityPrice not found with id: " + id));
    }

    @Override
    public ResortFacilityPriceResponse getById(Long resortFacilityId, Long id) {
        ResortFacilityPriceEntity entity = getEntityById(resortFacilityId, id);
        return new ResortFacilityPriceResponse(buildDto(entity));
    }

    @Override
    public PaginatedResponse<ResortFacilityPriceDto> getAll(Long resortFacilityId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortFacilityPriceDto> page = resortFacilityPriceRepository
                .findByResortFacilityEntity_IdAndIsActiveAndIsDeleted(resortFacilityId, true, false, pageable)
                .map(this::buildDto);
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortFacilityPriceEntity entity, UpdateResortFacilityPriceRequest request) {
        ResortFacilityPriceMapper.update(entity, request);
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

    private ResortFacilityPriceDto buildDto(ResortFacilityPriceEntity entity) {
        ResortFacilityPriceDto.ResortFacilityPriceDtoBuilder builder = ResortFacilityPriceMapper.toDto(entity)
                .resortFacility(ResortFacilityMapper.toDto(entity.getResortFacilityEntity()).build())
                .priceType(FacilityPriceTypeMapper.toDto(entity.getFacilityPriceTypeEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build());
        if (entity.getPriceUnitEntity() != null) {
            builder.priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build());
        }
        return builder.build();
    }
}
