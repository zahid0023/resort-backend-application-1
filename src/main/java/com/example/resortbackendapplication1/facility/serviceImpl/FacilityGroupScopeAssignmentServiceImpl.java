package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityGroupScopeAssignmentServiceImpl implements FacilityGroupScopeAssignmentService {
    private final FacilityGroupScopeAssignmentRepository facilityGroupScopeAssignmentRepository;

    public FacilityGroupScopeAssignmentServiceImpl(FacilityGroupScopeAssignmentRepository facilityGroupScopeAssignmentRepository) {
        this.facilityGroupScopeAssignmentRepository = facilityGroupScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityGroupEntity facilityGroupEntity,
                                  FacilityScopeEntity facilityScopeEntity) {
        if (facilityGroupScopeAssignmentRepository.existsByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
                facilityGroupEntity.getId(), facilityScopeEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityScope '" + facilityScopeEntity.getId()
                    + "' is already assigned to FacilityGroup '" + facilityGroupEntity.getId() + "'");
        }

        FacilityGroupScopeAssignmentEntity entity = FacilityGroupScopeAssignmentMapper.create();
        facilityGroupEntity.addFacilityGroupScopeAssignmentEntity(entity);
        facilityScopeEntity.addFacilityGroupScopeAssignmentEntity(entity);
        facilityGroupScopeAssignmentRepository.save(entity);
        log.info("FacilityGroupScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupScopeAssignmentEntity getEntityByFacilityScopeId(Long facilityGroupId, Long facilityScopeId) {
        return facilityGroupScopeAssignmentRepository
                .findByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(facilityGroupId, facilityScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FacilityGroupScopeAssignment not found for FacilityGroup '" + facilityGroupId
                                + "' and FacilityScope '" + facilityScopeId + "'"));
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
