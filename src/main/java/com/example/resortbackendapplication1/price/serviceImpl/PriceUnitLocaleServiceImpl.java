package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.CreatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.priceunit.priceunitlocale.UpdatePriceUnitLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceUnitLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceUnitLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PriceUnitLocaleServiceImpl implements PriceUnitLocaleService {

    private final PriceUnitLocaleRepository priceUnitLocaleRepository;

    public PriceUnitLocaleServiceImpl(PriceUnitLocaleRepository priceUnitLocaleRepository) {
        this.priceUnitLocaleRepository = priceUnitLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(PriceUnitEntity priceUnitEntity,
                                  LocaleEntity localeEntity,
                                  CreatePriceUnitLocaleRequest request) {
        PriceUnitLocaleEntity entity = PriceUnitLocaleMapper.create(request, priceUnitEntity, localeEntity);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceUnitLocaleEntity entity, UpdatePriceUnitLocaleRequest request) {
        PriceUnitLocaleMapper.update(entity, request);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceUnitLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceUnitLocaleRepository.save(entity);
        log.info("PriceUnitLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceUnitLocaleEntity getEntityById(Long priceUnitId, Long id) {
        return priceUnitLocaleRepository
                .findByPriceUnitEntity_IdAndIdAndIsActiveAndIsDeleted(priceUnitId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceUnitLocale not found with id: " + id));
    }
}
