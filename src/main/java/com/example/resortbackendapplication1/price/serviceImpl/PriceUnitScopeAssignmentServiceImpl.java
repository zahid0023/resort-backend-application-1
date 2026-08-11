package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitScopeAssignmentMapper;
import com.example.resortbackendapplication1.price.repository.PriceUnitScopeAssignmentRepository;
import com.example.resortbackendapplication1.price.service.PriceUnitScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PriceUnitScopeAssignmentServiceImpl implements PriceUnitScopeAssignmentService {
    private final PriceUnitScopeAssignmentRepository priceUnitScopeAssignmentRepository;

    public PriceUnitScopeAssignmentServiceImpl(PriceUnitScopeAssignmentRepository priceUnitScopeAssignmentRepository) {
        this.priceUnitScopeAssignmentRepository = priceUnitScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(PriceUnitEntity priceUnitEntity,
                                  PriceScopeEntity priceScopeEntity) {
        if (priceUnitScopeAssignmentRepository.existsByPriceUnitEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
                priceUnitEntity.getId(), priceScopeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceScope '" + priceScopeEntity.getId()
                    + "' is already assigned to PriceUnit '" + priceUnitEntity.getId() + "'");
        }

        PriceUnitScopeAssignmentEntity entity = PriceUnitScopeAssignmentMapper.create();
        priceUnitEntity.addPriceUnitScopeAssignmentEntity(entity);
        priceScopeEntity.addPriceUnitScopeAssignmentEntity(entity);
        priceUnitScopeAssignmentRepository.save(entity);
        log.info("PriceUnitScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitScopeAssignmentEntity getEntityByPriceScopeId(Long priceUnitId, Long priceScopeId) {
        return priceUnitScopeAssignmentRepository
                .findByPriceUnitEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(priceUnitId, priceScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "PriceUnitScopeAssignment not found for PriceUnit '" + priceUnitId
                                + "' and PriceScope '" + priceScopeId + "'"));
    }

    @Transactional
    @Override
    public SuccessResponse unassign(PriceUnitScopeAssignmentEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceUnitScopeAssignmentRepository.save(entity);
        log.info("PriceUnitScopeAssignment soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
