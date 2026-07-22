package com.example.resortbackendapplication1.bedtype.serviceImpl;

import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.CreateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.dto.request.bedtype.bedtypelocale.UpdateBedTypeLocaleRequest;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeLocaleEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeLocaleMapper;
import com.example.resortbackendapplication1.bedtype.repository.BedTypeLocaleRepository;
import com.example.resortbackendapplication1.bedtype.service.BedTypeLocaleService;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BedTypeLocaleServiceImpl implements BedTypeLocaleService {

    private final BedTypeLocaleRepository bedTypeLocaleRepository;

    public BedTypeLocaleServiceImpl(BedTypeLocaleRepository bedTypeLocaleRepository) {
        this.bedTypeLocaleRepository = bedTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(BedTypeEntity bedTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreateBedTypeLocaleRequest request) {
        BedTypeLocaleEntity entity = BedTypeLocaleMapper.create(request, bedTypeEntity, localeEntity);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(BedTypeLocaleEntity entity, UpdateBedTypeLocaleRequest request) {
        BedTypeLocaleMapper.update(entity, request);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(BedTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        bedTypeLocaleRepository.save(entity);
        log.info("BedTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BedTypeLocaleEntity getEntityById(Long bedTypeId, Long id) {
        return bedTypeLocaleRepository
                .findByBedTypeEntity_IdAndIdAndIsActiveAndIsDeleted(bedTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BedTypeLocale not found with id: " + id));
    }
}
