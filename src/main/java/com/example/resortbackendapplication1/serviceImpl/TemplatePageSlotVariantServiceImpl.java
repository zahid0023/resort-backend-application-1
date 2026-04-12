package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.templatepageslotvariants.CreateTemplatePageSlotVariantRequest;
import com.example.resortbackendapplication1.dto.request.templatepageslotvariants.UpdateTemplatePageSlotVariantRequest;
import com.example.resortbackendapplication1.dto.response.templatepageslotvariants.TemplatePageSlotVariantResponse;
import com.example.resortbackendapplication1.model.dto.TemplatePageSlotVariantDto;
import com.example.resortbackendapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortbackendapplication1.model.entity.TemplatePageSlotVariantEntity;
import com.example.resortbackendapplication1.model.entity.UiBlockDefinitionEntity;
import com.example.resortbackendapplication1.model.mapper.TemplatePageSlotVariantMapper;
import com.example.resortbackendapplication1.repository.TemplatePageSlotVariantRepository;
import com.example.resortbackendapplication1.service.TemplatePageSlotVariantService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplatePageSlotVariantServiceImpl implements TemplatePageSlotVariantService {

    private final TemplatePageSlotVariantRepository templatePageSlotVariantRepository;

    public TemplatePageSlotVariantServiceImpl(TemplatePageSlotVariantRepository templatePageSlotVariantRepository) {
        this.templatePageSlotVariantRepository = templatePageSlotVariantRepository;
    }

    @Override
    public SuccessResponse createTemplatePageSlotVariant(CreateTemplatePageSlotVariantRequest request,
                                                         TemplatePageSlotEntity templatePageSlotEntity,
                                                         UiBlockDefinitionEntity uiBlockDefinitionEntity) {
        TemplatePageSlotVariantEntity entity = TemplatePageSlotVariantMapper.fromRequest(request, templatePageSlotEntity, uiBlockDefinitionEntity);
        entity = templatePageSlotVariantRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public TemplatePageSlotVariantEntity getTemplatePageSlotVariantEntity(Long templateId, Long templatePageId,
                                                                          Long templatePageSlotId, Long id) {
        return templatePageSlotVariantRepository.findTemplatePageSlotVariantEntityBy(templateId, templatePageId, templatePageSlotId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Template Page Slot Variant with id: " + id + " was not found."));
    }

    @Override
    public TemplatePageSlotVariantResponse getTemplatePageSlotVariant(Long templateId, Long templatePageId,
                                                                      Long templatePageSlotId, Long id) {
        TemplatePageSlotVariantEntity entity = getTemplatePageSlotVariantEntity(templateId, templatePageId, templatePageSlotId, id);
        TemplatePageSlotVariantDto dto = TemplatePageSlotVariantMapper.toDto(entity);
        return new TemplatePageSlotVariantResponse(dto);
    }

    @Override
    public PaginatedResponse<TemplatePageSlotVariantDto> getAllTemplatePageSlotVariants(Long templateId, Long templatePageId,
                                                                                        Long templatePageSlotId, Pageable pageable) {
        Page<@NonNull TemplatePageSlotVariantEntity> page = templatePageSlotVariantRepository
                .findTemplatePageSlotVariantEntitiesBy(templateId, templatePageId, templatePageSlotId, true, false, pageable);
        Page<@NonNull TemplatePageSlotVariantDto> dtoPage = page.map(TemplatePageSlotVariantMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateTemplatePageSlotVariant(UpdateTemplatePageSlotVariantRequest request,
                                                         TemplatePageSlotVariantEntity templatePageSlotVariantEntity,
                                                         UiBlockDefinitionEntity uiBlockDefinitionEntity) {
        TemplatePageSlotVariantMapper.updateEntity(templatePageSlotVariantEntity, request, null, uiBlockDefinitionEntity);
        templatePageSlotVariantEntity = templatePageSlotVariantRepository.save(templatePageSlotVariantEntity);
        TemplatePageSlotVariantDto dto = TemplatePageSlotVariantMapper.toDto(templatePageSlotVariantEntity);
        return new SuccessResponse(true, dto.getId());
    }

    @Override
    public SuccessResponse deleteTemplatePageSlotVariant(Long templateId, Long templatePageId,
                                                         Long templatePageSlotId, Long id) {
        TemplatePageSlotVariantEntity entity = getTemplatePageSlotVariantEntity(templateId, templatePageId, templatePageSlotId, id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        templatePageSlotVariantRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
