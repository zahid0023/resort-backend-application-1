package com.example.resortbackendapplication1.resortpermissiontype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.CreateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.dto.request.resortpermissiontype.locale.UpdateResortPermissionTypeLocaleRequest;
import com.example.resortbackendapplication1.resortpermissiontype.model.dto.ResortPermissionTypeLocaleDto;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeLocaleEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.mapper.ResortPermissionTypeLocaleMapper;
import com.example.resortbackendapplication1.resortpermissiontype.repository.ResortPermissionTypeLocaleRepository;
import com.example.resortbackendapplication1.resortpermissiontype.service.ResortPermissionTypeLocaleService;
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
public class ResortPermissionTypeLocaleServiceImpl implements ResortPermissionTypeLocaleService {
    private final ResortPermissionTypeLocaleRepository resortPermissionTypeLocaleRepository;

    public ResortPermissionTypeLocaleServiceImpl(ResortPermissionTypeLocaleRepository resortPermissionTypeLocaleRepository) {
        this.resortPermissionTypeLocaleRepository = resortPermissionTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortPermissionTypeLocaleRequest request,
                                  ResortPermissionTypeEntity resortPermissionTypeEntity,
                                  LocaleEntity localeEntity) {
        if (resortPermissionTypeLocaleRepository.existsByResortPermissionTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                resortPermissionTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ResortPermissionType already has a locale entry for locale id: " + localeEntity.getId());
        }

        if (resortPermissionTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("ResortPermissionTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        ResortPermissionTypeLocaleEntity entity = ResortPermissionTypeLocaleMapper.create(request);
        resortPermissionTypeEntity.addResortPermissionTypeLocaleEntity(entity);
        localeEntity.addResortPermissionTypeLocaleEntity(entity);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortPermissionTypeLocaleEntity entity,
                                  UpdateResortPermissionTypeLocaleRequest request) {
        if (resortPermissionTypeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("ResortPermissionTypeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        ResortPermissionTypeLocaleMapper.update(entity, request);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortPermissionTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortPermissionTypeLocaleRepository.save(entity);
        log.info("ResortPermissionTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortPermissionTypeLocaleEntity getEntityById(Long resortPermissionTypeId, Long id) {
        return resortPermissionTypeLocaleRepository
                .findByResortPermissionTypeEntity_IdAndIdAndIsActiveAndIsDeleted(resortPermissionTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortPermissionTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ResortPermissionTypeLocaleDto> getAll(Long resortPermissionTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ResortPermissionTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? resortPermissionTypeLocaleRepository.findByResortPermissionTypeEntity_IdAndIsActiveAndIsDeleted(resortPermissionTypeId, true, false, pageable)
                : resortPermissionTypeLocaleRepository.findByResortPermissionTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        resortPermissionTypeId, localeCode, true, false, pageable))
                .map(ResortPermissionTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
