package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeScopeAssignmentEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceScopeEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeScopeAssignmentMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeScopeAssignmentRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeScopeAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PriceTypeScopeAssignmentServiceImpl implements PriceTypeScopeAssignmentService {
    private final PriceTypeScopeAssignmentRepository priceTypeScopeAssignmentRepository;

    public PriceTypeScopeAssignmentServiceImpl(PriceTypeScopeAssignmentRepository priceTypeScopeAssignmentRepository) {
        this.priceTypeScopeAssignmentRepository = priceTypeScopeAssignmentRepository;
    }

    @Transactional
    @Override
    public SuccessResponse assign(PriceTypeEntity priceTypeEntity,
                                  PriceScopeEntity priceScopeEntity) {
        if (priceTypeScopeAssignmentRepository.existsByPriceTypeEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(
                priceTypeEntity.getId(), priceScopeEntity.getId(), true, false)) {
            throw new IllegalStateException("PriceScope '" + priceScopeEntity.getId()
                    + "' is already assigned to PriceType '" + priceTypeEntity.getId() + "'");
        }

        PriceTypeScopeAssignmentEntity entity = PriceTypeScopeAssignmentMapper.create();
        priceTypeEntity.addPriceTypeScopeAssignmentEntity(entity);
        priceScopeEntity.addPriceTypeScopeAssignmentEntity(entity);
        priceTypeScopeAssignmentRepository.save(entity);
        log.info("PriceTypeScopeAssignment created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeScopeAssignmentEntity getEntityByPriceScopeId(Long priceTypeId, Long priceScopeId) {
        return priceTypeScopeAssignmentRepository
                .findByPriceTypeEntity_IdAndPriceScopeEntity_IdAndIsActiveAndIsDeleted(priceTypeId, priceScopeId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "PriceTypeScopeAssignment not found for PriceType '" + priceTypeId
                                + "' and PriceScope '" + priceScopeId + "'"));
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
