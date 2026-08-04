package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupScopeAssignmentService;
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
public class FacilityGroupScopeAssignmentServiceImpl implements FacilityGroupScopeAssignmentService {
    private final FacilityGroupScopeAssignmentRepository facilityGroupScopeAssignmentRepository;

    public FacilityGroupScopeAssignmentServiceImpl(FacilityGroupScopeAssignmentRepository facilityGroupScopeAssignmentRepository) {
        this.facilityGroupScopeAssignmentRepository = facilityGroupScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityScopeEntity facilityScopeEntity,
                                  FacilityGroupEntity facilityGroupEntity) {
        if (facilityGroupScopeAssignmentRepository.existsByFacilityScopeEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
                facilityScopeEntity.getId(), facilityGroupEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityGroup '" + facilityGroupEntity.getId()
                    + "' is already assigned to FacilityScope '" + facilityScopeEntity.getId() + "'");
        }

        FacilityGroupScopeAssignmentEntity entity = FacilityGroupScopeAssignmentMapper.create();
        facilityScopeEntity.addFacilityGroupScopeAssignmentEntity(entity);
        facilityGroupEntity.addFacilityGroupScopeAssignmentEntity(entity);
        facilityGroupScopeAssignmentRepository.save(entity);
        log.info("FacilityGroupScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupScopeAssignmentEntity getEntityById(Long facilityScopeId, Long id) {
        return facilityGroupScopeAssignmentRepository
                .findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityGroupScopeAssignment not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityGroupScopeAssignmentDto> getAll(Long facilityScopeId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityGroupScopeAssignmentDto> dtoPage = facilityGroupScopeAssignmentRepository
                .findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(facilityScopeId, true, false, pageable)
                .map(entity -> {
                    FacilityGroupDto facilityGroupDto = FacilityGroupMapper.toDto(entity.getFacilityGroupEntity()).build();
                    return FacilityGroupScopeAssignmentMapper.toDto(entity)
                            .facilityGroup(facilityGroupDto)
                            .build();
                });
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse unassign(FacilityGroupScopeAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupScopeAssignmentRepository.save(entity);
        log.info("FacilityGroupScopeAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
