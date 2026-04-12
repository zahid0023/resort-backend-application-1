package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.facilities.CreateFacilityRequest;
import com.example.resortapplication1.dto.request.facilities.UpdateFacilityRequest;
import com.example.resortapplication1.dto.response.facilities.FacilityResponse;
import com.example.resortapplication1.model.dto.FacilityDto;
import com.example.resortapplication1.model.entity.FacilityEntity;
import com.example.resortapplication1.model.entity.FacilityGroupEntity;
import com.example.resortapplication1.model.mapper.FacilityMapper;
import com.example.resortapplication1.repository.FacilityRepository;
import com.example.resortapplication1.service.FacilityGroupService;
import com.example.resortapplication1.service.FacilityService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityGroupService facilityGroupService;

    public FacilityServiceImpl(FacilityRepository facilityRepository,
                               FacilityGroupService facilityGroupService) {
        this.facilityRepository = facilityRepository;
        this.facilityGroupService = facilityGroupService;
    }

    @Override
    public SuccessResponse createFacility(CreateFacilityRequest request) {
        FacilityGroupEntity facilityGroup = facilityGroupService.getFacilityGroupEntity(request.getFacilityGroupId());
        FacilityEntity entity = FacilityMapper.fromRequest(request, facilityGroup);
        entity = facilityRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityEntity getFacilityEntity(Long id) {
        return facilityRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Facility with id: " + id + " was not found."));
    }

    @Override
    public FacilityResponse getFacility(Long id) {
        FacilityEntity entity = getFacilityEntity(id);
        FacilityDto dto = FacilityMapper.toDto(entity);
        return new FacilityResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityDto> getAllFacilities(Pageable pageable) {
        Page<@NonNull FacilityEntity> page = facilityRepository.findAllByIsActiveAndIsDeleted(true, false, pageable);
        Page<@NonNull FacilityDto> dtoPage = page.map(FacilityMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateFacility(Long id, UpdateFacilityRequest request) {
        FacilityEntity entity = getFacilityEntity(id);
        FacilityGroupEntity facilityGroup = request.getFacilityGroupId() != null
                ? facilityGroupService.getFacilityGroupEntity(request.getFacilityGroupId())
                : null;
        FacilityMapper.updateEntity(entity, request, facilityGroup);
        entity = facilityRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteFacility(Long id) {
        FacilityEntity entity = getFacilityEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
