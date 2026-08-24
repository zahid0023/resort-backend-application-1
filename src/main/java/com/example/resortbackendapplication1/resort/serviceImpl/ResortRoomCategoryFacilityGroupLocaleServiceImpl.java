package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.CreateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortroomcategoryfacilitygroup.locale.UpdateResortRoomCategoryFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortroomcategoryfacilitygrouplocales.ResortRoomCategoryFacilityGroupLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortRoomCategoryFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.model.mapper.ResortRoomCategoryFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRoomCategoryFacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.resort.service.ResortRoomCategoryFacilityGroupLocaleService;
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
public
class ResortRoomCategoryFacilityGroupLocaleServiceImpl implements ResortRoomCategoryFacilityGroupLocaleService {

    private final ResortRoomCategoryFacilityGroupLocaleRepository resortRoomCategoryFacilityGroupLocaleRepository;

    public ResortRoomCategoryFacilityGroupLocaleServiceImpl(ResortRoomCategoryFacilityGroupLocaleRepository resortRoomCategoryFacilityGroupLocaleRepository) {
        this.resortRoomCategoryFacilityGroupLocaleRepository = resortRoomCategoryFacilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryFacilityGroupLocaleRequest request,
                                  ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomCategoryFacilityGroupLocaleRepository.existsByResortRoomCategoryFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomCategoryFacilityGroupEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomCategoryFacilityGroup already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomCategoryFacilityGroupLocaleEntity entity = ResortRoomCategoryFacilityGroupLocaleMapper.create(request);
        resortRoomCategoryFacilityGroupEntity.addResortRoomCategoryFacilityGroupLocaleEntity(entity);
        localeEntity.addResortRoomCategoryFacilityGroupLocaleEntity(entity);
        resortRoomCategoryFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroupLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryFacilityGroupLocaleEntity getEntityById(Long resortRoomCategoryFacilityGroupId, Long id) {
        return resortRoomCategoryFacilityGroupLocaleRepository
                .findByResortRoomCategoryFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryFacilityGroupLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryFacilityGroupLocaleDto> getAll(Long resortRoomCategoryFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomCategoryFacilityGroupLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomCategoryFacilityGroupLocaleRepository.findByResortRoomCategoryFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityGroupId, true, false, pageable)
                : resortRoomCategoryFacilityGroupLocaleRepository.findByResortRoomCategoryFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomCategoryFacilityGroupId, localeCode, true, false, pageable))
                .map(ResortRoomCategoryFacilityGroupLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortRoomCategoryFacilityGroupLocaleCountResponse getActiveCount(Long resortRoomCategoryFacilityGroupId) {
        List<String> codes = resortRoomCategoryFacilityGroupLocaleRepository
                .findLocaleCodeByResortRoomCategoryFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortRoomCategoryFacilityGroupId, true, false);
        return new ResortRoomCategoryFacilityGroupLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryFacilityGroupLocaleEntity entity, UpdateResortRoomCategoryFacilityGroupLocaleRequest request) {
        ResortRoomCategoryFacilityGroupLocaleMapper.update(entity, request);
        resortRoomCategoryFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroupLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryFacilityGroupLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomCategoryFacilityGroupLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
