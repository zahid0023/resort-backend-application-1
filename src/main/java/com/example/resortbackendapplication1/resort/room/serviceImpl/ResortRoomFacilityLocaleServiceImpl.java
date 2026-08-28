package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.CreateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomfacility.locale.UpdateResortRoomFacilityLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomfacilitylocales.ResortRoomFacilityLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomFacilityLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomFacilityLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomFacilityLocaleMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomFacilityLocaleRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomFacilityLocaleService;
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
public class ResortRoomFacilityLocaleServiceImpl implements ResortRoomFacilityLocaleService {

    private final ResortRoomFacilityLocaleRepository resortRoomFacilityLocaleRepository;

    public ResortRoomFacilityLocaleServiceImpl(ResortRoomFacilityLocaleRepository resortRoomFacilityLocaleRepository) {
        this.resortRoomFacilityLocaleRepository = resortRoomFacilityLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomFacilityLocaleRequest request,
                                  ResortRoomFacilityEntity resortRoomFacilityEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomFacilityLocaleRepository.existsByResortRoomFacilityEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomFacilityEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoomFacility already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomFacilityLocaleEntity entity = ResortRoomFacilityLocaleMapper.create(request);
        resortRoomFacilityEntity.addResortRoomFacilityLocaleEntity(entity);
        localeEntity.addResortRoomFacilityLocaleEntity(entity);
        resortRoomFacilityLocaleRepository.save(entity);
        log.info("ResortRoomFacilityLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomFacilityLocaleEntity getEntityById(Long resortRoomFacilityId, Long id) {
        return resortRoomFacilityLocaleRepository
                .findByResortRoomFacilityEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomFacilityId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomFacilityLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoomFacilityLocaleDto> getAll(Long resortRoomFacilityId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomFacilityLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomFacilityLocaleRepository.findByResortRoomFacilityEntity_IdAndIsActiveAndIsDeleted(resortRoomFacilityId, true, false, pageable)
                : resortRoomFacilityLocaleRepository.findByResortRoomFacilityEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomFacilityId, localeCode, true, false, pageable))
                .map(ResortRoomFacilityLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortRoomFacilityLocaleCountResponse getActiveCount(Long resortRoomFacilityId) {
        List<String> codes = resortRoomFacilityLocaleRepository
                .findLocaleCodeByResortRoomFacilityEntity_IdAndIsActiveAndIsDeleted(resortRoomFacilityId, true, false);
        return new ResortRoomFacilityLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomFacilityLocaleEntity entity, UpdateResortRoomFacilityLocaleRequest request) {
        ResortRoomFacilityLocaleMapper.update(entity, request);
        resortRoomFacilityLocaleRepository.save(entity);
        log.info("ResortRoomFacilityLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomFacilityLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomFacilityLocaleRepository.save(entity);
        log.info("ResortRoomFacilityLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
