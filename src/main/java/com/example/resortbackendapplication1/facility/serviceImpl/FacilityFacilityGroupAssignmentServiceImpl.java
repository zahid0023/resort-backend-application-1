package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityFacilityGroupAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityFacilityGroupAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityFacilityGroupAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityFacilityGroupAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacilityFacilityGroupAssignmentServiceImpl implements FacilityFacilityGroupAssignmentService {
    private final FacilityFacilityGroupAssignmentRepository facilityFacilityGroupAssignmentRepository;

    public FacilityFacilityGroupAssignmentServiceImpl(FacilityFacilityGroupAssignmentRepository facilityFacilityGroupAssignmentRepository) {
        this.facilityFacilityGroupAssignmentRepository = facilityFacilityGroupAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityEntity facilityEntity,
                                  FacilityGroupEntity facilityGroupEntity) {
        if (facilityFacilityGroupAssignmentRepository.existsByFacilityEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(
                facilityEntity.getId(), facilityGroupEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityGroup '" + facilityGroupEntity.getId()
                    + "' is already assigned to Facility '" + facilityEntity.getId() + "'");
        }

        validateScopeCompatibility(facilityGroupEntity, facilityEntity);

        FacilityFacilityGroupAssignmentEntity entity = FacilityFacilityGroupAssignmentMapper.create();
        facilityEntity.addFacilityFacilityGroupAssignmentEntity(entity);
        facilityGroupEntity.addFacilityFacilityGroupAssignmentEntity(entity);
        facilityFacilityGroupAssignmentRepository.save(entity);
        log.info("FacilityFacilityGroupAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void validateScopeCompatibility(FacilityGroupEntity facilityGroupEntity, FacilityEntity facilityEntity) {
        Set<Long> allowedScopeIds = facilityGroupEntity.getFacilityGroupScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(assignment -> assignment.getFacilityScopeEntity().getId())
                .collect(Collectors.toSet());

        List<String> unsupportedCodes = facilityEntity.getFacilityScopeAssignmentEntities().stream()
                .filter(assignment -> Boolean.TRUE.equals(assignment.getIsActive())
                        && Boolean.FALSE.equals(assignment.getIsDeleted()))
                .map(assignment -> assignment.getFacilityScopeEntity())
                .filter(facilityScopeEntity -> !allowedScopeIds.contains(facilityScopeEntity.getId()))
                .map(FacilityScopeEntity::getCode)
                .toList();

        if (!unsupportedCodes.isEmpty()) {
            throw new IllegalStateException("FacilityGroup '" + facilityGroupEntity.getCode()
                    + "' is not scoped to: " + unsupportedCodes);
        }
    }

    @Override
    public FacilityFacilityGroupAssignmentEntity getEntityByFacilityGroupId(Long facilityId, Long facilityGroupId) {
        return facilityFacilityGroupAssignmentRepository
                .findByFacilityEntity_IdAndFacilityGroupEntity_IdAndIsActiveAndIsDeleted(facilityId, facilityGroupId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FacilityFacilityGroupAssignment not found for Facility '" + facilityId
                                + "' and FacilityGroup '" + facilityGroupId + "'"));
    }

    @Transactional
    @Override
    public SuccessResponse unassign(FacilityFacilityGroupAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityFacilityGroupAssignmentRepository.save(entity);
        log.info("FacilityFacilityGroupAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
