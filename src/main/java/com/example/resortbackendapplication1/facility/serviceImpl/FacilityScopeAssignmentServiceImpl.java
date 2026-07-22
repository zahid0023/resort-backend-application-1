package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentId;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FacilityScopeAssignmentServiceImpl implements FacilityScopeAssignmentService {

    private final FacilityScopeAssignmentRepository repository;

    public FacilityScopeAssignmentServiceImpl(FacilityScopeAssignmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityEntity facilityEntity, FacilityScopeEntity facilityScopeEntity) {
        FacilityScopeAssignmentId compositeId =
                new FacilityScopeAssignmentId(facilityEntity.getId(), facilityScopeEntity.getId());

        Optional<FacilityScopeAssignmentEntity> existing = repository.findById(compositeId);

        FacilityScopeAssignmentEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            if (Boolean.TRUE.equals(entity.getIsActive()) && Boolean.FALSE.equals(entity.getIsDeleted())) {
                throw new IllegalStateException("FacilityScope is already assigned to this Facility.");
            }
            entity.setIsActive(true);
            entity.setIsDeleted(false);
            entity.setDeletedBy(null);
            entity.setDeletedAt(null);
        } else {
            entity = FacilityScopeAssignmentMapper.create(facilityEntity, facilityScopeEntity);
        }

        repository.save(entity);
        log.info("FacilityScopeAssignment assigned: facilityId={}, facilityScopeId={}",
                facilityEntity.getId(), facilityScopeEntity.getId());
        return new SuccessResponse(true, facilityEntity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse unassign(Long facilityId, Long facilityScopeId) {
        FacilityScopeAssignmentEntity entity = getEntityById(facilityId, facilityScopeId);
        entity.setIsActive(false);
        entity.setIsDeleted(true);
        repository.save(entity);
        log.info("FacilityScopeAssignment unassigned: facilityId={}, facilityScopeId={}", facilityId, facilityScopeId);
        return new SuccessResponse(true, facilityId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacilityScopeAssignmentDto> getAll(Long facilityId) {
        return repository.findByFacilityEntity_IdAndIsActiveAndIsDeleted(facilityId, true, false)
                .stream()
                .map(FacilityScopeAssignmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FacilityScopeAssignmentEntity getEntityById(Long facilityId, Long facilityScopeId) {
        return repository.findByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
                        facilityId, facilityScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FacilityScopeAssignment not found for facilityId: " + facilityId
                                + " and facilityScopeId: " + facilityScopeId));
    }
}
