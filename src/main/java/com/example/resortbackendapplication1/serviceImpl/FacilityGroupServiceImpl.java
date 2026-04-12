package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.facilitygroups.CreateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.facilitygroups.UpdateFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.facilitygroups.FacilityGroupResponse;
import com.example.resortbackendapplication1.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.repository.FacilityGroupRepository;
import com.example.resortbackendapplication1.service.FacilityGroupService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FacilityGroupServiceImpl implements FacilityGroupService {

    private final FacilityGroupRepository facilityGroupRepository;

    public FacilityGroupServiceImpl(FacilityGroupRepository facilityGroupRepository) {
        this.facilityGroupRepository = facilityGroupRepository;
    }

    @Override
    public SuccessResponse createFacilityGroup(CreateFacilityGroupRequest request) {
        FacilityGroupEntity entity = FacilityGroupMapper.fromRequest(request);
        entity = facilityGroupRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupEntity getFacilityGroupEntity(Long id) {
        return facilityGroupRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Facility Group with id: " + id + " was not found."));
    }

    @Override
    public FacilityGroupResponse getFacilityGroup(Long id) {
        FacilityGroupEntity entity = getFacilityGroupEntity(id);
        FacilityGroupDto dto = FacilityGroupMapper.toDto(entity);
        return new FacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<FacilityGroupDto> getAllFacilityGroups(Pageable pageable) {
        Page<@NonNull FacilityGroupEntity> page = facilityGroupRepository.findAllByIsActiveAndIsDeleted(true, false, pageable);
        Page<@NonNull FacilityGroupDto> dtoPage = page.map(FacilityGroupMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateFacilityGroup(Long id, UpdateFacilityGroupRequest request) {
        FacilityGroupEntity entity = getFacilityGroupEntity(id);
        FacilityGroupMapper.updateEntity(entity, request);
        entity = facilityGroupRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deleteFacilityGroup(Long id) {
        FacilityGroupEntity entity = getFacilityGroupEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
