package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.CreateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.locale.UpdateResortRoomLocaleRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortroomlocales.ResortRoomLocaleCountResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomLocaleDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomLocaleMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomLocaleRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomLocaleService;
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
public class ResortRoomLocaleServiceImpl implements ResortRoomLocaleService {

    private final ResortRoomLocaleRepository resortRoomLocaleRepository;

    public ResortRoomLocaleServiceImpl(ResortRoomLocaleRepository resortRoomLocaleRepository) {
        this.resortRoomLocaleRepository = resortRoomLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomLocaleRequest request,
                                  ResortRoomEntity resortRoomEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoomLocaleRepository.existsByResortRoomEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoomEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoom already has a locale entry for locale id: " + localeEntity.getId());
        }

        ResortRoomLocaleEntity entity = ResortRoomLocaleMapper.create(request);
        resortRoomEntity.addResortRoomLocaleEntity(entity);
        localeEntity.addResortRoomLocaleEntity(entity);
        resortRoomLocaleRepository.save(entity);
        log.info("ResortRoomLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomLocaleEntity getEntityById(Long resortRoomId, Long id) {
        return resortRoomLocaleRepository
                .findByResortRoomEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoomLocaleDto> getAll(Long resortRoomId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoomLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoomLocaleRepository.findByResortRoomEntity_IdAndIsActiveAndIsDeleted(resortRoomId, true, false, pageable)
                : resortRoomLocaleRepository.findByResortRoomEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoomId, localeCode, true, false, pageable))
                .map(ResortRoomLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortRoomLocaleCountResponse getActiveCount(Long resortRoomId) {
        List<String> codes = resortRoomLocaleRepository
                .findLocaleCodeByResortRoomEntity_IdAndIsActiveAndIsDeleted(resortRoomId, true, false);
        return new ResortRoomLocaleCountResponse((long) codes.size(), codes);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomLocaleEntity entity, UpdateResortRoomLocaleRequest request) {
        ResortRoomLocaleMapper.update(entity, request);
        resortRoomLocaleRepository.save(entity);
        log.info("ResortRoomLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomLocaleRepository.save(entity);
        log.info("ResortRoomLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
