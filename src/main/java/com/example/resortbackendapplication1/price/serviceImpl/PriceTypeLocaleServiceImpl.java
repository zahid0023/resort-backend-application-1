package com.example.resortbackendapplication1.price.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.CreatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.dto.request.pricetype.pricetypelocale.UpdatePriceTypeLocaleRequest;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeLocaleEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceTypeLocaleMapper;
import com.example.resortbackendapplication1.price.repository.PriceTypeLocaleRepository;
import com.example.resortbackendapplication1.price.service.PriceTypeLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PriceTypeLocaleServiceImpl implements PriceTypeLocaleService {

    private final PriceTypeLocaleRepository priceTypeLocaleRepository;

    public PriceTypeLocaleServiceImpl(PriceTypeLocaleRepository priceTypeLocaleRepository) {
        this.priceTypeLocaleRepository = priceTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(PriceTypeEntity priceTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreatePriceTypeLocaleRequest request) {
        PriceTypeLocaleEntity entity = PriceTypeLocaleMapper.create(request, priceTypeEntity, localeEntity);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PriceTypeLocaleEntity entity, UpdatePriceTypeLocaleRequest request) {
        PriceTypeLocaleMapper.update(entity, request);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PriceTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeLocaleRepository.save(entity);
        log.info("PriceTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeLocaleEntity getEntityById(Long priceTypeId, Long id) {
        return priceTypeLocaleRepository
                .findByPriceTypeEntity_IdAndIdAndIsActiveAndIsDeleted(priceTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PriceTypeLocale not found with id: " + id));
    }
}
