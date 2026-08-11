package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FacilityScopeAssignmentServiceImpl implements FacilityScopeAssignmentService {
    private final FacilityScopeAssignmentRepository facilityScopeAssignmentRepository;

    public FacilityScopeAssignmentServiceImpl(FacilityScopeAssignmentRepository facilityScopeAssignmentRepository) {
        this.facilityScopeAssignmentRepository = facilityScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityEntity facilityEntity,
                                  FacilityScopeEntity facilityScopeEntity) {
        if (facilityScopeAssignmentRepository.existsByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(
                facilityEntity.getId(), facilityScopeEntity.getId(), true, false)) {
            throw new IllegalStateException("FacilityScope '" + facilityScopeEntity.getId()
                    + "' is already assigned to Facility '" + facilityEntity.getId() + "'");
        }

        FacilityScopeAssignmentEntity entity = FacilityScopeAssignmentMapper.create();
        facilityEntity.addFacilityScopeAssignmentEntity(entity);
        facilityScopeEntity.addFacilityScopeAssignmentEntity(entity);
        facilityScopeAssignmentRepository.save(entity);
        log.info("FacilityScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityScopeAssignmentEntity getEntityByFacilityScopeId(Long facilityId, Long facilityScopeId) {
        return facilityScopeAssignmentRepository
                .findByFacilityEntity_IdAndFacilityScopeEntity_IdAndIsActiveAndIsDeleted(facilityId, facilityScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FacilityScopeAssignment not found for Facility '" + facilityId
                                + "' and FacilityScope '" + facilityScopeId + "'"));
    }

    @Transactional
    @Override
    public SuccessResponse unassign(FacilityScopeAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityScopeAssignmentRepository.save(entity);
        log.info("FacilityScopeAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
