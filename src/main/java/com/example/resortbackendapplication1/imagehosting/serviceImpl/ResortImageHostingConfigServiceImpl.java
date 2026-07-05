package com.example.resortbackendapplication1.imagehosting.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.imagehosting.dto.request.CreateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.request.UpdateResortImageHostingConfigRequest;
import com.example.resortbackendapplication1.imagehosting.dto.response.ResortImageHostingConfigResponse;
import com.example.resortbackendapplication1.imagehosting.enums.ResortImageHostingConfigSortField;
import com.example.resortbackendapplication1.imagehosting.model.dto.ResortImageHostingConfigDto;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.model.mapper.ResortImageHostingConfigMapper;
import com.example.resortbackendapplication1.imagehosting.model.projection.ResortImageHostingConfigSummary;
import com.example.resortbackendapplication1.imagehosting.repository.ResortImageHostingConfigRepository;
import com.example.resortbackendapplication1.imagehosting.service.ResortImageHostingConfigService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortImageHostingConfigServiceImpl implements ResortImageHostingConfigService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortImageHostingConfigSortField.allowedFields();

    private final ResortImageHostingConfigRepository repository;

    public ResortImageHostingConfigServiceImpl(ResortImageHostingConfigRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortImageHostingConfigRequest request) {
        request.getProvider().validate(request.getConfig());
        ResortImageHostingConfigEntity entity = ResortImageHostingConfigMapper.create(request);
        repository.save(entity);
        log.info("ResortImageHostingConfig created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortImageHostingConfigEntity getEntityById(Long id) {
        return repository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortImageHostingConfig not found with id: " + id));
    }

    @Override
    public ResortImageHostingConfigResponse getById(Long id) {
        ResortImageHostingConfigEntity entity = getEntityById(id);
        ResortImageHostingConfigDto dto = ResortImageHostingConfigMapper.toDto(entity);
        return new ResortImageHostingConfigResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortImageHostingConfigSummary> getAll(PaginatedRequest request) {
        Page<@NonNull ResortImageHostingConfigSummary> page = repository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortImageHostingConfigEntity entity,
                                  UpdateResortImageHostingConfigRequest request) {
        ResortImageHostingConfigMapper.update(entity, request);
        repository.save(entity);
        log.info("ResortImageHostingConfig updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ResortImageHostingConfigEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        repository.save(entity);
        log.info("ResortImageHostingConfig soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
