package com.example.resortbackendapplication1.facility.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.facility.model.dto.FacilityDto;
import com.example.resortbackendapplication1.facility.model.dto.FacilityScopeAssignmentDto;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeAssignmentEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityMapper;
import com.example.resortbackendapplication1.facility.model.mapper.FacilityScopeAssignmentMapper;
import com.example.resortbackendapplication1.facility.repository.FacilityScopeAssignmentRepository;
import com.example.resortbackendapplication1.facility.service.FacilityScopeAssignmentService;
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
public class FacilityScopeAssignmentServiceImpl implements FacilityScopeAssignmentService {
    private final FacilityScopeAssignmentRepository facilityScopeAssignmentRepository;

    public FacilityScopeAssignmentServiceImpl(FacilityScopeAssignmentRepository facilityScopeAssignmentRepository) {
        this.facilityScopeAssignmentRepository = facilityScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(FacilityScopeEntity facilityScopeEntity,
                                  FacilityEntity facilityEntity) {
        if (facilityScopeAssignmentRepository.existsByFacilityScopeEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted(
                facilityScopeEntity.getId(), facilityEntity.getId(), true, false)) {
            throw new IllegalStateException("Facility '" + facilityEntity.getId()
                    + "' is already assigned to FacilityScope '" + facilityScopeEntity.getId() + "'");
        }

        FacilityScopeAssignmentEntity entity = FacilityScopeAssignmentMapper.create();
        facilityScopeEntity.addFacilityScopeAssignmentEntity(entity);
        facilityEntity.addFacilityScopeAssignmentEntity(entity);
        facilityScopeAssignmentRepository.save(entity);
        log.info("FacilityScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public FacilityScopeAssignmentEntity getEntityById(Long facilityScopeId, Long id) {
        return facilityScopeAssignmentRepository
                .findByFacilityScopeEntity_IdAndIdAndIsActiveAndIsDeleted(facilityScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("FacilityScopeAssignment not found with id: " + id));
    }

    @Override
    public PaginatedResponse<FacilityScopeAssignmentDto> getAll(Long facilityScopeId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull FacilityScopeAssignmentDto> dtoPage = facilityScopeAssignmentRepository
                .findByFacilityScopeEntity_IdAndIsActiveAndIsDeleted(facilityScopeId, true, false, pageable)
                .map(entity -> {
                    FacilityDto facilityDto = FacilityMapper.toDto(entity.getFacilityEntity()).build();
                    return FacilityScopeAssignmentMapper.toDto(entity)
                            .facility(facilityDto)
                            .build();
                });
        return Pagination.buildPaginatedResponse(dtoPage);
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
