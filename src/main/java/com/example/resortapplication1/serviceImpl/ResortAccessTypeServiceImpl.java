package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.resortaccesstypes.CreateResortAccessTypeRequest;
import com.example.resortapplication1.dto.request.resortaccesstypes.UpdateResortAccessTypeRequest;
import com.example.resortapplication1.dto.response.resortaccesstypes.ResortAccessTypeResponse;
import com.example.resortapplication1.model.dto.ResortAccessTypeDto;
import com.example.resortapplication1.model.entity.ResortAccessTypeEntity;
import com.example.resortapplication1.model.mapper.ResortAccessTypeMapper;
import com.example.resortapplication1.model.projection.ResortAccessTypeSummary;
import com.example.resortapplication1.repository.ResortAccessTypeRepository;
import com.example.resortapplication1.service.ResortAccessTypeService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResortAccessTypeServiceImpl implements ResortAccessTypeService {

    private final ResortAccessTypeRepository resortAccessTypeRepository;

    public ResortAccessTypeServiceImpl(ResortAccessTypeRepository resortAccessTypeRepository) {
        this.resortAccessTypeRepository = resortAccessTypeRepository;
    }

    @Override
    public SuccessResponse createResortAccessType(CreateResortAccessTypeRequest request) {
        ResortAccessTypeEntity entity = ResortAccessTypeMapper.fromRequest(request);
        entity = resortAccessTypeRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortAccessTypeResponse getResortAccessType(Long id) {
        ResortAccessTypeEntity entity = getResortAccessTypeEntity(id);
        return new ResortAccessTypeResponse(ResortAccessTypeMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortAccessTypeDto> getAllResortAccessTypes(Pageable pageable) {
        Page<@NonNull ResortAccessTypeSummary> page = resortAccessTypeRepository
                .findAllByIsActiveAndIsDeleted(true, false, pageable, ResortAccessTypeSummary.class);

        Page<@NonNull ResortAccessTypeDto> dtoPage = page.map(p ->
                ResortAccessTypeDto.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .name(p.getName())
                        .description(p.getDescription())
                        .build()
        );
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateResortAccessType(Long id, UpdateResortAccessTypeRequest request) {
        ResortAccessTypeEntity entity = getResortAccessTypeEntity(id);
        ResortAccessTypeMapper.updateEntity(entity, request);
        entity = resortAccessTypeRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteResortAccessType(Long id) {
        ResortAccessTypeEntity entity = getResortAccessTypeEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortAccessTypeRepository.save(entity);
        return new SuccessResponse(true, id);
    }

    @Override
    public ResortAccessTypeEntity getResortAccessTypeEntity(Long id) {
        return resortAccessTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Access Type with id: " + id + " was not found."));
    }

    @Override
    public ResortAccessTypeEntity getResortAccessTypeByCode(String code) {
        return resortAccessTypeRepository.findByCodeAndIsActiveAndIsDeleted(code, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Access Type with code: " + code + " was not found."));
    }
}
