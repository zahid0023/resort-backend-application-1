package com.example.resortbackendapplication1.pagetype.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.UpdatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;
import com.example.resortbackendapplication1.pagetype.model.mapper.PageTypeLocaleMapper;
import com.example.resortbackendapplication1.pagetype.repository.PageTypeLocaleRepository;
import com.example.resortbackendapplication1.pagetype.service.PageTypeLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PageTypeLocaleServiceImpl implements PageTypeLocaleService {

    private final PageTypeLocaleRepository pageTypeLocaleRepository;

    public PageTypeLocaleServiceImpl(PageTypeLocaleRepository pageTypeLocaleRepository) {
        this.pageTypeLocaleRepository = pageTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(PageTypeEntity pageTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreatePageTypeLocaleRequest request) {
        PageTypeLocaleEntity entity = PageTypeLocaleMapper.create(request, pageTypeEntity, localeEntity);
        pageTypeLocaleRepository.save(entity);
        log.info("PageTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(PageTypeLocaleEntity entity, UpdatePageTypeLocaleRequest request) {
        PageTypeLocaleMapper.update(entity, request);
        pageTypeLocaleRepository.save(entity);
        log.info("PageTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(PageTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        pageTypeLocaleRepository.save(entity);
        log.info("PageTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PageTypeLocaleEntity getEntityById(Long pageTypeId, Long id) {
        return pageTypeLocaleRepository
                .findByPageTypeEntity_IdAndIdAndIsActiveAndIsDeleted(pageTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("PageTypeLocale not found with id: " + id));
    }
}
