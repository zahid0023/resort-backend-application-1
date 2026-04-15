package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortfacilities.BulkCreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.CreateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilities.UpdateResortFacilityRequest;
import com.example.resortbackendapplication1.dto.response.resortfacilities.ResortFacilityResponse;
import com.example.resortbackendapplication1.model.dto.ResortFacilityDto;
import com.example.resortbackendapplication1.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.model.mapper.ResortFacilityMapper;
import com.example.resortbackendapplication1.repository.ResortFacilityRepository;
import com.example.resortbackendapplication1.service.ResortFacilityService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResortFacilityServiceImpl implements ResortFacilityService {

    private final ResortFacilityRepository resortFacilityRepository;

    public ResortFacilityServiceImpl(ResortFacilityRepository resortFacilityRepository) {
        this.resortFacilityRepository = resortFacilityRepository;
    }

    @Override
    public SuccessResponse createResortFacility(CreateResortFacilityRequest request,
                                                FacilityEntity facilityEntity,
                                                ResortFacilityGroupEntity resortFacilityGroupEntity) {
        ResortFacilityEntity entity = ResortFacilityMapper.fromRequest(request, facilityEntity, resortFacilityGroupEntity);
        entity = resortFacilityRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse bulkCreateResortFacilities(BulkCreateResortFacilityRequest request,
                                                      ResortFacilityGroupEntity resortFacilityGroupEntity,
                                                      List<FacilityEntity> facilityEntities) {
        List<ResortFacilityEntity> entities = new ArrayList<>();
        List<CreateResortFacilityRequest> facilities = request.getFacilities();
        for (int i = 0; i < facilities.size(); i++) {
            entities.add(ResortFacilityMapper.fromRequest(facilities.get(i), facilityEntities.get(i), resortFacilityGroupEntity));
        }
        resortFacilityRepository.saveAll(entities);
        return new SuccessResponse(true, 0L);
    }

    @Override
    public ResortFacilityEntity getResortFacilityEntity(Long resortFacilityGroupId, Long id) {
        return resortFacilityRepository.findByResortFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(resortFacilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Facility with id: " + id + " was not found."));
    }

    @Override
    public ResortFacilityResponse getResortFacility(Long resortFacilityGroupId, Long id) {
        ResortFacilityEntity entity = getResortFacilityEntity(resortFacilityGroupId, id);
        return new ResortFacilityResponse(ResortFacilityMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortFacilityDto> getAllResortFacilities(Long resortFacilityGroupId, Pageable pageable) {
        Page<@NonNull ResortFacilityEntity> page = resortFacilityRepository
                .findAllByResortFacilityGroupEntity_IdAndIsActiveAndIsDeleted(resortFacilityGroupId, true, false, pageable);
        Page<@NonNull ResortFacilityDto> dtoPage = page.map(ResortFacilityMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateResortFacility(ResortFacilityEntity resortFacilityEntity, UpdateResortFacilityRequest request) {
        ResortFacilityMapper.updateEntity(resortFacilityEntity, request);
        resortFacilityEntity = resortFacilityRepository.save(resortFacilityEntity);
        return new SuccessResponse(true, resortFacilityEntity.getId());
    }

    @Override
    public SuccessResponse deleteResortFacility(Long resortFacilityGroupId, Long id) {
        ResortFacilityEntity entity = getResortFacilityEntity(resortFacilityGroupId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
