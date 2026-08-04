package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.CreateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.dto.request.facilityscope.locale.UpdateFacilityScopeLocaleRequest;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeLocaleDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeLocaleEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeLocaleMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeLocaleRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
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
public class FacilityScopeLocaleServiceImpl implements FacilityScopeLocaleService {
    private final FacilityScopeLocaleRepository facilityScopeLocaleRepository;

    public FacilityScopeLocaleServiceImpl(FacilityScopeLocaleRepository facilityScopeLocaleRepository) {
        this.facilityScopeLocaleRepository = facilityScopeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateFacilityScopeLocaleRequest request,
                                  FacilityScopeEntity facilityScopeEntity,
                                  LocaleEntity localeEntity) {
        if (facilityScopeLocaleRepository.existsByFacilityScopeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                facilityScopeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityScopeLocale already exists for facilityScopeId '"
                    + facilityScopeEntity.getId() + "' and localeId '" + localeEntity.getId() + "'");
        }

        if (facilityScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIsActiveAndIsDeleted(
                localeEntity.getId(), request.getName(), true, false)) {
            throw new IllegalStateException("FacilityScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + localeEntity.getId() + "'");
        }

        FacilityScopeLocaleEntity entity = FacilityScopeLocaleMapper.create(request);
        facilityScopeEntity.addFacilityScopeLocaleEntity(entity);
        localeEntity.addFacilityScopeLocaleEntity(entity);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(FacilityScopeLocaleEntity entity,
                                  UpdateFacilityScopeLocaleRequest request) {
        if (facilityScopeLocaleRepository.existsByLocaleEntity_IdAndNameAndIdNotAndIsActiveAndIsDeleted(
                entity.getLocaleEntity().getId(), request.getName(), entity.getId(), true, false)) {
            throw new IllegalStateException("FacilityScopeLocale with name '" + request.getName()
                    + "' already exists for localeId '" + entity.getLocaleEntity().getId() + "'");
        }

        FacilityScopeLocaleMapper.update(entity, request);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(FacilityScopeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityScopeLocaleRepository.save(entity);
        log.info("FacilityScopeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityScopeLocaleEntity getEntityById(Long facilityScopeId, Long id) {
        return facilityScopeLocaleRepository
                .findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityScopeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityScopeLocaleDto> getAll(Long facilityScopeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityScopeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? facilityScopeLocaleRepository.findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(facilityScopeId, true, false, pageable)
                : facilityScopeLocaleRepository.findByFacilityScopeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        facilityScopeId, localeCode, true, false, pageable))
                .map(FacilityScopeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
