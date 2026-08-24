package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.locale.CreateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacility.locale.UpdateResortRoomCategoryFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilitylocales.ResortRoomCategoryFacilityLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryFacilityLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityLocaleService;
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
public class ResortRoomCategoryFacilityLocaleServiceImpl implements ResortRoomCategoryFacilityLocaleService {

    private final ResortRoomCategoryFacilityLocaleRepository resortRoomCategoryFacilityLocaleRepository;

    public ResortRoomCategoryFacilityLocaleServiceImpl(ResortRoomCategoryFacilityLocaleRepository resortRoomCategoryFacilityLocaleRepository) {
        this.resortRoomCategoryFacilityLocaleRepository = resortRoomCategoryFacilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryFacilityLocaleRequest request,
                                  ResortRoomCategoryFacilityEntity resortRoomCategoryFacilityEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomCategoryFacilityLocaleRepository.existsByResortRoomCategoryFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomCategoryFacilityEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomCategoryFacility already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomCategoryFacilityLocaleEntity entity = ResortRoomCategoryFacilityLocaleMapper.create(request);
        resortRoomCategoryFacilityEntity.addResortRoomCategoryFacilityLocaleEntity(entity);
        localeEntity.addResortRoomCategoryFacilityLocaleEntity(entity);
        resortRoomCategoryFacilityLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryFacilityLocaleEntity getEntityById(Long resortRoomCategoryFacilityId, Long id) {
        return resortRoomCategoryFacilityLocaleRepository
                .findByResortRoomCategoryFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryFacilityLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryFacilityLocaleDto> getAll(Long resortRoomCategoryFacilityId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomCategoryFacilityLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomCategoryFacilityLocaleRepository.findByResortRoomCategoryFacilityEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityId, true, false, pageable)
                : resortRoomCategoryFacilityLocaleRepository.findByResortRoomCategoryFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomCategoryFacilityId, localeCode, true, false, pageable))
                .map(ResortRoomCategoryFacilityLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortRoomCategoryFacilityLocaleCountResponse getActiveCount(Long resortRoomCategoryFacilityId) {
        List<String> codes = resortRoomCategoryFacilityLocaleRepository
                .findLocaleCodeByResortRoomCategoryFacilityEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityId, true, false);
        return new ResortRoomCategoryFacilityLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryFacilityLocaleEntity entity, UpdateResortRoomCategoryFacilityLocaleRequest request) {
        ResortRoomCategoryFacilityLocaleMapper.update(entity, request);
        resortRoomCategoryFacilityLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryFacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryFacilityLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
