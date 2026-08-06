package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityGroupFacilityAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupFacilityAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityGroupFacilityAssignmentMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityGroupFacilityAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityGroupFacilityAssignmentService;
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
public class FacilityGroupFacilityAssignmentServiceImpl implements FacilityGroupFacilityAssignmentService {
    private final FacilityGroupFacilityAssignmentRepository facilityGroupFacilityAssignmentRepository;

    public FacilityGroupFacilityAssignmentServiceImpl(FacilityGroupFacilityAssignmentRepository facilityGroupFacilityAssignmentRepository) {
        this.facilityGroupFacilityAssignmentRepository = facilityGroupFacilityAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityGroupEntity facilityGroupEntity,
                                  FacilityEntity facilityEntity) {
        if (facilityGroupFacilityAssignmentRepository.existsByFacilityGroupEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                facilityGroupEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("Facility '" + facilityEntity.getId()
                    + "' is already assigned to FacilityGroup '" + facilityGroupEntity.getId() + "'");
        }

        FacilityGroupFacilityAssignmentEntity entity = FacilityGroupFacilityAssignmentMapper.create();
        facilityGroupEntity.addFacilityGroupFacilityAssignmentEntity(entity);
        facilityEntity.addFacilityGroupFacilityAssignmentEntity(entity);
        facilityGroupFacilityAssignmentRepository.save(entity);
        log.info("FacilityGroupFacilityAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityGroupFacilityAssignmentEntity getEntityById(Long facilityGroupId, Long id) {
        return facilityGroupFacilityAssignmentRepository
                .findByFacilityGroupEntity_IdAndIdAndIsActiveAndIsDeleted(facilityGroupId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityGroupFacilityAssignment not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityGroupFacilityAssignmentDto> getAll(Long facilityGroupId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityGroupFacilityAssignmentDto> dtoPage = facilityGroupFacilityAssignmentRepository
                .findByFacilityGroupEntity_IdAndIsActiveAndIsDeleted(facilityGroupId, true, false, pageable)
                .map(entity -> {
                    FacilityDto facilityDto = FacilityMapper.toDto(entity.getFacilityEntity()).build();
                    return FacilityGroupFacilityAssignmentMapper.toDto(entity)
                            .facility(facilityDto)
                            .build();
                });
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse unassign(FacilityGroupFacilityAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        facilityGroupFacilityAssignmentRepository.save(entity);
        log.info("FacilityGroupFacilityAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
