package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.CreateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacilitygroup.locale.UpdateResortRoomFacilityGroupLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitygrouplocales.ResortRoomFacilityGroupLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityGroupLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityGroupLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityGroupLocaleMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomFacilityGroupLocaleRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityGroupLocaleService;
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
public class ResortRoomFacilityGroupLocaleServiceImpl implements ResortRoomFacilityGroupLocaleService {

    private final ResortRoomFacilityGroupLocaleRepository resortRoomFacilityGroupLocaleRepository;

    public ResortRoomFacilityGroupLocaleServiceImpl(ResortRoomFacilityGroupLocaleRepository resortRoomFacilityGroupLocaleRepository) {
        this.resortRoomFacilityGroupLocaleRepository = resortRoomFacilityGroupLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomFacilityGroupLocaleRequest request,
                                  ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomFacilityGroupLocaleRepository.existsByResortRoomFacilityGroupEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomFacilityGroupEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomFacilityGroup already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomFacilityGroupLocaleEntity entity = ResortRoomFacilityGroupLocaleMapper.create(request);
        resortRoomFacilityGroupEntity.addResortRoomFacilityGroupLocaleEntity(entity);
        localeEntity.addResortRoomFacilityGroupLocaleEntity(entity);
        resortRoomFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomFacilityGroupLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomFacilityGroupLocaleEntity getEntityById(Long resortRoomFacilityGroupId, Long id) {
        return resortRoomFacilityGroupLocaleRepository
                .findByResortRoomFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomFacilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomFacilityGroupLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoomFacilityGroupLocaleDto> getAll(Long resortRoomFacilityGroupId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomFacilityGroupLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomFacilityGroupLocaleRepository.findByResortRoomFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortRoomFacilityGroupId, true, false, pageable)
                : resortRoomFacilityGroupLocaleRepository.findByResortRoomFacilityGroupEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomFacilityGroupId, localeCode, true, false, pageable))
                .map(ResortRoomFacilityGroupLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortRoomFacilityGroupLocaleCountResponse getActiveCount(Long resortRoomFacilityGroupId) {
        List<String> codes = resortRoomFacilityGroupLocaleRepository
                .findLocaleCodeByResortRoomFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortRoomFacilityGroupId, true, false);
        return new ResortRoomFacilityGroupLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomFacilityGroupLocaleEntity entity, UpdateResortRoomFacilityGroupLocaleRequest request) {
        ResortRoomFacilityGroupLocaleMapper.update(entity, request);
        resortRoomFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomFacilityGroupLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomFacilityGroupLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomFacilityGroupLocaleRepository.save(entity);
        log.info("ResortRoomFacilityGroupLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
