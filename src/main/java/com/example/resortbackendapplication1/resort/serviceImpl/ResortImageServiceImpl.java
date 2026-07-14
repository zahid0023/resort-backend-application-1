package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.imagehosting.dto.request.ImageRequest;
import com.example.resortbackendapplication1.imagehosting.model.entity.ResortImageHostingConfigEntity;
import com.example.resortbackendapplication1.imagehosting.service.ImageUploadService;
import com.example.resortbackendapplication1.resort.dto.request.resortimage.UpdateResortImageRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortImageResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortImageEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortImageSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortImageMapper;
import com.example.resortbackendapplication1.resort.model.projection.ResortImageSummary;
import com.example.resortbackendapplication1.resort.repository.ResortImageRepository;
import com.example.resortbackendapplication1.resort.service.ResortImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ResortImageServiceImpl implements ResortImageService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortImageSortField.allowedFields();

    private final ResortImageRepository resortImageRepository;
    private final ImageUploadService imageUploadService;

    public ResortImageServiceImpl(ResortImageRepository resortImageRepository,
                                  ImageUploadService imageUploadService) {
        this.resortImageRepository = resortImageRepository;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    @Override
    public List<ResortImageDto> createAll(List<ImageRequest> imageRequests,
                                          ResortImageHostingConfigEntity config) {
        List<ResortImageEntity> entities = imageRequests.stream()
                .map(req -> ResortImageMapper.create(req, config))
                .toList();
        resortImageRepository.saveAll(entities);
        log.info("ResortImages created: {} images", entities.size());
        return entities.stream().map(ResortImageMapper::toDto).toList();
    }

    @Override
    public ResortImageEntity getEntityById(Long id) {
        return resortImageRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort image not found with id: " + id));
    }

    @Override
    public ResortImageResponse getById(Long id) {
        ResortImageDto dto = ResortImageMapper.toDto(getEntityById(id));
        return new ResortImageResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortImageSummary> getAll(Long resortId, PaginatedRequest request) {
        Page<ResortImageSummary> page = resortImageRepository.findAllByResortEntity_IdAndIsActiveAndIsDeleted(
                resortId, true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortImageEntity entity, UpdateResortImageRequest request) {
        ResortImageMapper.update(entity, request);
        resortImageRepository.save(entity);
        log.info("ResortImage updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortImageEntity entity) {
        if (entity.getExternalId() != null) {
            imageUploadService.delete(
                    entity.getExternalId(),
                    entity.getConfigEntity().getProvider(),
                    entity.getConfigEntity().getConfig()
            );
        }
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortImageRepository.save(entity);
        log.info("ResortImage soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
