package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.price.model.dto.PriceTypeScopeAssignmentDto;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeScopeAssignmentMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeScopeAssignmentRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeAssignmentService;
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
public class PriceTypeScopeAssignmentServiceImpl implements PriceTypeScopeAssignmentService {
    private final PriceTypeScopeAssignmentRepository priceTypeScopeAssignmentRepository;

    public PriceTypeScopeAssignmentServiceImpl(PriceTypeScopeAssignmentRepository priceTypeScopeAssignmentRepository) {
        this.priceTypeScopeAssignmentRepository = priceTypeScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(PriceTypeScopeEntity priceTypeScopeEntity,
                                  PriceTypeEntity priceTypeEntity) {
        if (priceTypeScopeAssignmentRepository.existsByPriceTypeScopeEntity_IdAndPriceTypeEntity_IdAndIsActiveAndIsDeleted(
                priceTypeScopeEntity.getId(), priceTypeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceType '" + priceTypeEntity.getId()
                    + "' is already assigned to PriceTypeScope '" + priceTypeScopeEntity.getId() + "'");
        }

        PriceTypeScopeAssignmentEntity entity = PriceTypeScopeAssignmentMapper.create();
        priceTypeScopeEntity.addPriceTypeScopeAssignmentEntity(entity);
        priceTypeEntity.addPriceTypeScopeAssignmentEntity(entity);
        priceTypeScopeAssignmentRepository.save(entity);
        log.info("PriceTypeScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeScopeAssignmentEntity getEntityById(Long priceTypeScopeId, Long id) {
        return priceTypeScopeAssignmentRepository
                .findByPriceTypeScopeEntity_IdAndIdAndIsActiveAndIsDeleted(priceTypeScopeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceTypeScopeAssignment not found with id: " + id));
    }

    @Override
    public PaginatedResponse<PriceTypeScopeAssignmentDto> getAll(Long priceTypeScopeId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull PriceTypeScopeAssignmentDto> dtoPage = priceTypeScopeAssignmentRepository
                .findByPriceTypeScopeEntity_IdAndIsActiveAndIsDeleted(priceTypeScopeId, true, false, pageable)
                .map(entity -> {
                    PriceTypeDto priceTypeDto = PriceTypeMapper.toDto(entity.getPriceTypeEntity()).build();
                    return PriceTypeScopeAssignmentMapper.toDto(entity)
                            .priceType(priceTypeDto)
                            .build();
                });
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse unassign(PriceTypeScopeAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeScopeAssignmentRepository.save(entity);
        log.info("PriceTypeScopeAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
