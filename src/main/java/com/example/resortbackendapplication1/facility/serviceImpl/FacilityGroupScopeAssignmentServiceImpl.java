package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupScopeAssignmentId;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FacilityGroupScopeAssignmentServiceImpl implements FacilityGroupScopeAssignmentService {

    private final FacilityGroupScopeAssignmentRepository repository;

    public FacilityGroupScopeAssignmentServiceImpl(FacilityGroupScopeAssignmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityGroupEntity facilityGroupEntity, FacilityScopeEntity facilityScopeEntity) {
        FacilityGroupScopeAssignmentId compositeId =
                new FacilityGroupScopeAssignmentId(facilityGroupEntity.getId(), facilityScopeEntity.getId());

        Optional<FacilityGroupScopeAssignmentEntity> existing = repository.findById(compositeId);

        FacilityGroupScopeAssignmentEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            if (Boolean.TRUE.equals(entity.getIsActive()) && Boolean.FALSE.equals(entity.getIsDeleted())) {
                throw new IllegalStateException("FacilityScope is already assigned to this FacilityGroup.");
            }
            entity.setIsActive(true);
            entity.setIsDeleted(false);
            entity.setDeletedBy(null);
        } else {
            entity = FacilityGroupScopeAssignmentMapper.create(facilityGroupEntity, facilityScopeEntity);
        }

        repository.save(entity);
        log.info("FacilityGroupScopeAssignment assigned: facilityGroupId={}, facilityScopeId={}",
                facilityGroupEntity.getId(), facilityScopeEntity.getId());
        return new SuccessResponse(true, facilityGroupEntity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse unassign(Long facilityGroupId, Long facilityScopeId) {
        FacilityGroupScopeAssignmentEntity entity = getEntityById(facilityGroupId, facilityScopeId);
        entity.setIsActive(false);
        entity.setIsDeleted(true);
        repository.save(entity);
        log.info("FacilityGroupScopeAssignment unassigned: facilityGroupId={}, facilityScopeId={}",
                facilityGroupId, facilityScopeId);
        return new SuccessResponse(true, facilityGroupId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacilityGroupScopeAssignmentDto> getAll(Long facilityGroupId) {
        return repository.findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(facilityGroupId, true, false)
                .stream()
                .map(FacilityGroupScopeAssignmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityGroupScopeAssignmentEntity getEntityById(Long facilityGroupId, Long facilityScopeId) {
        return repository.findByFacilityGroupEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
                        facilityGroupId, facilityScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FacilityGroupScopeAssignment not found for facilityGroupId: " + facilityGroupId
                                + " and facilityScopeId: " + facilityScopeId));
    }
}
