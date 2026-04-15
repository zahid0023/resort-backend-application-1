package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.CreateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.request.resortfacilitygroups.UpdateResortFacilityGroupRequest;
import com.example.resortbackendapplication1.dto.response.resortfacilitygroups.ResortFacilityGroupResponse;
import com.example.resortbackendapplication1.model.dto.ResortFacilityGroupDto;
import com.example.resortbackendapplication1.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.model.entity.ResortEntity;
import com.example.resortbackendapplication1.model.entity.ResortFacilityGroupEntity;
import com.example.resortbackendapplication1.model.mapper.ResortFacilityGroupMapper;
import com.example.resortbackendapplication1.repository.ResortFacilityGroupRepository;
import com.example.resortbackendapplication1.service.ResortFacilityGroupService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResortFacilityGroupServiceImpl implements ResortFacilityGroupService {

    private final ResortFacilityGroupRepository resortFacilityGroupRepository;
    public ResortFacilityGroupServiceImpl(ResortFacilityGroupRepository resortFacilityGroupRepository) {
        this.resortFacilityGroupRepository = resortFacilityGroupRepository;
    }

    @Override
    public SuccessResponse createResortFacilityGroup(CreateResortFacilityGroupRequest request,
                                                     ResortEntity resortEntity,
                                                     FacilityGroupEntity facilityGroupEntity) {
        ResortFacilityGroupEntity entity = ResortFacilityGroupMapper.fromRequest(request, resortEntity, facilityGroupEntity);
        entity = resortFacilityGroupRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortFacilityGroupEntity getResortFacilityGroupEntity(Long id) {
        return resortFacilityGroupRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Facility Group with id: " + id + " was not found."));
    }

    @Override
    public ResortFacilityGroupEntity getResortFacilityGroupEntity(Long resortId, Long id) {
        return resortFacilityGroupRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort Facility Group with id: " + id + " was not found."));
    }

    @Override
    public ResortFacilityGroupResponse getResortFacilityGroup(Long resortId, Long id) {
        ResortFacilityGroupEntity entity = getResortFacilityGroupEntity(resortId, id);
        ResortFacilityGroupDto dto = ResortFacilityGroupMapper.toDto(entity);
        return new ResortFacilityGroupResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortFacilityGroupDto> getAllResortFacilityGroups(Long id, Pageable pageable) {
        Page<@NonNull ResortFacilityGroupEntity> page = resortFacilityGroupRepository.findAllByResortEntity_IdAndIsActiveAndIsDeleted(id, true, false, pageable);
        Page<@NonNull ResortFacilityGroupDto> dtoPage = page.map(ResortFacilityGroupMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateResortFacilityGroup(ResortFacilityGroupEntity resortFacilityGroupEntity, UpdateResortFacilityGroupRequest request) {
        ResortFacilityGroupMapper.updateEntity(resortFacilityGroupEntity, request);
        resortFacilityGroupEntity = resortFacilityGroupRepository.save(resortFacilityGroupEntity);
        return new SuccessResponse(true, resortFacilityGroupEntity.getId());
    }

    @Override
    public SuccessResponse deleteResortFacilityGroup(Long resortId, Long id) {
        ResortFacilityGroupEntity entity = getResortFacilityGroupEntity(resortId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortFacilityGroupRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
