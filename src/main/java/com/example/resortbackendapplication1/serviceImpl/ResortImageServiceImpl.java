package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.response.resortimages.ResortImageResponse;
import com.example.resortbackendapplication1.model.dto.ResortImageDto;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortImageEntity;
import com.example.resortbackendapplication1.model.mapper.ResortImageMapper;
import com.example.resortbackendapplication1.repository.ResortImageRepository;
import com.example.resortbackendapplication1.service.ResortImageService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ResortImageServiceImpl implements ResortImageService {

    private final ResortImageRepository resortImageRepository;

    public ResortImageServiceImpl(ResortImageRepository resortImageRepository) {
        this.resortImageRepository = resortImageRepository;
    }

    @Override
    @Transactional
    public SuccessResponse saveImages(ResortEntity resortEntity, List<ImageRequest> imageRequests) {
        List<ResortImageEntity> resortImageEntities = imageRequests.stream()
                .map(imageRequest -> ResortImageMapper.fromRequest(imageRequest, resortEntity))
                .toList();
        resortImageRepository.saveAll(resortImageEntities);
        return new SuccessResponse(true, 0L);
    }

    @Override
    public ResortImageEntity getResortImageEntity(Long resortId, Long id) {
        return resortImageRepository
                .findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Resort image with id: " + id + " was not found."));
    }

    @Override
    public ResortImageResponse getResortImage(Long resortId, Long id) {
        return new ResortImageResponse(ResortImageMapper.toDto(getResortImageEntity(resortId, id)));
    }

    @Override
    public PaginatedResponse<ResortImageDto> getAllResortImages(Long resortId, Pageable pageable) {
        Page<@NonNull ResortImageEntity> page = resortImageRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable);
        Page<@NonNull ResortImageDto> dtoPage = page.map(ResortImageMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortImageResponse updateResortImage(ResortImageEntity entity, List<ImageRequest> imageRequests) {
        return new ResortImageResponse(ResortImageMapper.toDto(entity));
    }

    @Override
    public SuccessResponse deleteResortImage(Long resortId, Long id) {
        ResortImageEntity entity = getResortImageEntity(resortId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortImageRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
