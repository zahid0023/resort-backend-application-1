package com.example.resortbackendapplication1.resortroletype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.CreateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.dto.request.resortroletype.locale.UpdateResortRoleTypeLocaleRequest;
import com.example.resortbackendapplication1.resortroletype.model.dto.ResortRoleTypeLocaleDto;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeLocaleEntity;
import com.example.resortbackendapplication1.resortroletype.model.mapper.ResortRoleTypeLocaleMapper;
import com.example.resortbackendapplication1.resortroletype.repository.ResortRoleTypeLocaleRepository;
import com.example.resortbackendapplication1.resortroletype.service.ResortRoleTypeLocaleService;
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
public class ResortRoleTypeLocaleServiceImpl implements ResortRoleTypeLocaleService {
    private final ResortRoleTypeLocaleRepository resortRoleTypeLocaleRepository;

    public ResortRoleTypeLocaleServiceImpl(ResortRoleTypeLocaleRepository resortRoleTypeLocaleRepository) {
        this.resortRoleTypeLocaleRepository = resortRoleTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoleTypeLocaleRequest request,
                                  ResortRoleTypeEntity resortRoleTypeEntity,
                                  LocaleEntity localeEntity) {
        if (resortRoleTypeLocaleRepository.existsByResortRoleTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortRoleTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoleType already has a locale entry for locale id: " + localeEntity.getId());
        }

        if (resortRoleTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("ResortRoleTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        ResortRoleTypeLocaleEntity entity = ResortRoleTypeLocaleMapper.create(request);
        resortRoleTypeEntity.addResortRoleTypeLocaleEntity(entity);
        localeEntity.addResortRoleTypeLocaleEntity(entity);
        resortRoleTypeLocaleRepository.save(entity);
        log.info("ResortRoleTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoleTypeLocaleEntity entity,
                                  UpdateResortRoleTypeLocaleRequest request) {
        if (resortRoleTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("ResortRoleTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        ResortRoleTypeLocaleMapper.update(entity, request);
        resortRoleTypeLocaleRepository.save(entity);
        log.info("ResortRoleTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoleTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoleTypeLocaleRepository.save(entity);
        log.info("ResortRoleTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoleTypeLocaleEntity getEntityById(Long resortRoleTypeId, Long id) {
        return resortRoleTypeLocaleRepository
                .findByResortRoleTypeEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoleTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoleTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortRoleTypeLocaleDto> getAll(Long resortRoleTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortRoleTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortRoleTypeLocaleRepository.findByResortRoleTypeEntity_IdAndIsActiveAndIsDeleted(resortRoleTypeId, true, false, pageable)
                : resortRoleTypeLocaleRepository.findByResortRoleTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortRoleTypeId, localeCode, true, false, pageable))
                .map(ResortRoleTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
