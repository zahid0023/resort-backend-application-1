package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.UpdateResortImageStorageConfigRequest;
import com.example.resortbackendapplication1.dto.response.resortimagestorageconfigs.ResortImageStorageConfigResponse;
import com.example.resortbackendapplication1.model.dto.ResortImageStorageConfigDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageStorageConfigEntity;
import com.example.resortbackendapplication1.model.mapper.ResortImageStorageConfigMapper;
import com.example.resortbackendapplication1.repository.ResortImageStorageConfigRepository;
import com.example.resortbackendapplication1.service.ResortImageStorageConfigService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResortImageStorageConfigServiceImpl implements ResortImageStorageConfigService {

    private final ResortImageStorageConfigRepository resortImageStorageConfigRepository;

    public ResortImageStorageConfigServiceImpl(ResortImageStorageConfigRepository resortImageStorageConfigRepository) {
        this.resortImageStorageConfigRepository = resortImageStorageConfigRepository;
    }

    @Override
    public SuccessResponse createResortImageStorageConfig(CreateResortImageStorageConfigRequest request,
                                                          ResortEntity resortEntity) {
        request.getProvider().validate(request.getConfig());
        ResortImageStorageConfigEntity entity = ResortImageStorageConfigMapper.fromRequest(request, resortEntity);
        entity = resortImageStorageConfigRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortImageStorageConfigEntity getResortImageStorageConfigEntity(Long resortId, Long id) {
        return resortImageStorageConfigRepository
                .findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ResortImageStorageConfig with id: " + id + " was not found."));
    }

    @Override
    public ResortImageStorageConfigResponse getResortImageStorageConfig(Long resortId, Long id) {
        ResortImageStorageConfigEntity entity = getResortImageStorageConfigEntity(resortId, id);
        return new ResortImageStorageConfigResponse(ResortImageStorageConfigMapper.toDto(entity));
    }

    @Override
    public ResortImageStorageConfigResponse getResortImageStorageConfig(Long resortId) {
        ResortImageStorageConfigEntity resortImageStorageConfigEntity = resortImageStorageConfigRepository.findByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortImageStorageConfig with id: " + resortId + " was not found."));
        return new ResortImageStorageConfigResponse(ResortImageStorageConfigMapper.toDto(resortImageStorageConfigEntity));
    }

    @Override
    public PaginatedResponse<ResortImageStorageConfigDto> getAllResortImageStorageConfigs(Long resortId,
                                                                                          Pageable pageable) {
        Page<@NonNull ResortImageStorageConfigEntity> page = resortImageStorageConfigRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable);
        Page<@NonNull ResortImageStorageConfigDto> dtoPage = page.map(ResortImageStorageConfigMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateResortImageStorageConfig(ResortImageStorageConfigEntity entity,
                                                          UpdateResortImageStorageConfigRequest request) {
        if (request.getProvider() != null && request.getConfig() != null) {
            request.getProvider().validate(request.getConfig());
        } else if (request.getConfig() != null) {
            entity.getProvider().validate(request.getConfig());
        }
        ResortImageStorageConfigMapper.updateEntity(entity, request);
        entity = resortImageStorageConfigRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteResortImageStorageConfig(Long resortId, Long id) {
        ResortImageStorageConfigEntity entity = getResortImageStorageConfigEntity(resortId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortImageStorageConfigRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
