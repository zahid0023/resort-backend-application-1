package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.templatepageslots.CreateTemplatePageSlotRequest;
import com.example.resortapplication1.dto.request.templatepageslots.UpdateTemplatePageSlotRequest;
import com.example.resortapplication1.dto.response.templatepageslots.TemplatePageSlotResponse;
import com.example.resortapplication1.model.dto.TemplatePageSlotDto;
import com.example.resortapplication1.model.entity.TemplatePageEntity;
import com.example.resortapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortapplication1.model.entity.UiBlockCategoryEntity;
import com.example.resortapplication1.model.mapper.TemplatePageSlotMapper;
import com.example.resortapplication1.repository.TemplatePageSlotRepository;
import com.example.resortapplication1.service.TemplatePageService;
import com.example.resortapplication1.service.TemplatePageSlotService;
import com.example.resortapplication1.service.UiBlockCategoryService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplatePageSlotServiceImpl implements TemplatePageSlotService {

    private final TemplatePageSlotRepository templatePageSlotRepository;

    public TemplatePageSlotServiceImpl(TemplatePageSlotRepository templatePageSlotRepository) {
        this.templatePageSlotRepository = templatePageSlotRepository;
    }

    @Override
    public SuccessResponse createTemplatePageSlot(CreateTemplatePageSlotRequest request,
                                                  TemplatePageEntity templatePageEntity,
                                                  UiBlockCategoryEntity uiBlockCategoryEntity) {
        if (templatePageSlotRepository.existsByTemplatePageEntity_IdAndUiBlockCategoryEntity_IdAndIsActiveAndIsDeleted(
                templatePageEntity.getId(), uiBlockCategoryEntity.getId(), true, false)) {
            throw new IllegalArgumentException(
                    "An active template page slot with ui_block_category_id: " + uiBlockCategoryEntity.getId()
                    + " already exists for template_page_id: " + templatePageEntity.getId());
        }
        TemplatePageSlotEntity entity = TemplatePageSlotMapper.fromRequest(request, templatePageEntity, uiBlockCategoryEntity);
        entity = templatePageSlotRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public TemplatePageSlotEntity getTemplatePageSlotEntity(Long templateId, Long templatePageId, Long templatePageSlotId) {
        return templatePageSlotRepository.findTemplatePageSlotEntityBy(templateId, templatePageId, templatePageSlotId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("TemplatePageSlotEntity not found"));
    }

    @Override
    public TemplatePageSlotResponse getTemplatePageSlot(Long templateId, Long templatePageId, Long templatePageSlotId) {
        TemplatePageSlotEntity entity = getTemplatePageSlotEntity(templateId, templatePageId, templatePageSlotId);
        TemplatePageSlotDto dto = TemplatePageSlotMapper.toDto(entity);
        return new TemplatePageSlotResponse(dto);
    }

    @Override
    public PaginatedResponse<TemplatePageSlotDto> getAllTemplatePageSlots(Long templateId, Long templatePageId, Pageable pageable) {
        Page<@NonNull TemplatePageSlotEntity> page = templatePageSlotRepository.findTemplatePageSlotEntitiesBy(templateId, templatePageId, true, false, pageable);
        Page<@NonNull TemplatePageSlotDto> dtoPage = page.map(TemplatePageSlotMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updateTemplatePageSlot(UpdateTemplatePageSlotRequest request,
                                                  TemplatePageSlotEntity templatePageSlotEntity,
                                                  UiBlockCategoryEntity uiBlockCategoryEntity) {
        TemplatePageSlotMapper.updateEntity(templatePageSlotEntity, request, uiBlockCategoryEntity);
        templatePageSlotEntity = templatePageSlotRepository.save(templatePageSlotEntity);
        TemplatePageSlotDto dto = TemplatePageSlotMapper.toDto(templatePageSlotEntity);
        return new SuccessResponse(true, dto.getId());
    }

    @Override
    public SuccessResponse deleteTemplatePageSlot(Long templateId, Long templatePageId, Long templatePageSlotId) {
        TemplatePageSlotEntity entity = getTemplatePageSlotEntity(templateId, templatePageId, templatePageSlotId);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        templatePageSlotRepository.save(entity);
        return new SuccessResponse(true, templatePageSlotId);
    }
}
