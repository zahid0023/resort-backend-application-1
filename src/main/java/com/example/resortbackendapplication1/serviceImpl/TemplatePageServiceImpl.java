package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.templatepages.CreateTemplatePageRequest;
import com.example.resortbackendapplication1.dto.request.templatepages.UpdateTemplatePageRequest;
import com.example.resortbackendapplication1.dto.response.templatepages.TemplatePageResponse;
import com.example.resortbackendapplication1.model.dto.TemplatePageDto;
import com.example.resortbackendapplication1.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.model.entity.TemplateEntity;
import com.example.resortbackendapplication1.model.entity.TemplatePageEntity;
import com.example.resortbackendapplication1.model.mapper.TemplatePageMapper;
import com.example.resortbackendapplication1.repository.TemplatePageRepository;
import com.example.resortbackendapplication1.service.PageTypeService;
import com.example.resortbackendapplication1.service.TemplatePageService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplatePageServiceImpl implements TemplatePageService {

    private final TemplatePageRepository templatePageRepository;
    private final PageTypeService pageTypeService;

    public TemplatePageServiceImpl(TemplatePageRepository templatePageRepository,
                                   PageTypeService pageTypeService) {
        this.templatePageRepository = templatePageRepository;
        this.pageTypeService = pageTypeService;
    }

    @Override
    public SuccessResponse createTemplatePage(CreateTemplatePageRequest request, TemplateEntity templateEntity) {
        PageTypeEntity pageTypeEntity = pageTypeService.getPageTypeById(request.getPageTypeId());
        if (templatePageRepository.existsByTemplateEntity_IdAndPageTypeEntity_IdAndIsActiveAndIsDeleted(
                templateEntity.getId(), pageTypeEntity.getId(), true, false)) {
            throw new IllegalArgumentException(
                    "An active template page with page_type_id: " + pageTypeEntity.getId()
                    + " already exists for template_id: " + templateEntity.getId());
        }
        TemplatePageEntity entity = TemplatePageMapper.fromRequest(request, templateEntity, pageTypeEntity);
        entity = templatePageRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public TemplatePageResponse getTemplatePage(Long templateId, Long templatePageId) {
        TemplatePageEntity entity = getTemplatePageEntity(templateId, templatePageId);
        TemplatePageDto dto = TemplatePageMapper.toDto(entity);
        return new TemplatePageResponse(dto);
    }

    @Override
    public PaginatedResponse<TemplatePageDto> getAllTemplatePages(Long templateId, Pageable pageable) {
        Page<@NonNull TemplatePageEntity> page = templatePageRepository.findAllByTemplateEntity_IdAndIsActiveAndIsDeleted(templateId, true, false, pageable);
        Page<@NonNull TemplatePageDto> dtoPage = page.map(TemplatePageMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public TemplatePageResponse updateTemplatePage(UpdateTemplatePageRequest request, TemplatePageEntity templatePageEntity) {
        TemplatePageMapper.updateEntity(templatePageEntity, request);
        templatePageRepository.save(templatePageEntity);
        TemplatePageDto dto = TemplatePageMapper.toDto(templatePageEntity);
        return new TemplatePageResponse(dto);
    }

    @Override
    public SuccessResponse deleteTemplatePage(Long templateId, Long templatePageId) {
        TemplatePageEntity entity = getTemplatePageEntity(templateId, templatePageId);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        templatePageRepository.save(entity);
        return new SuccessResponse(true, templatePageId);
    }

    @Override
    public TemplatePageEntity getTemplatePageEntity(Long templateId, Long templatePageId) {
        return templatePageRepository.findByTemplateEntity_IdAndIdAndIsActiveAndIsDeleted(templateId, templatePageId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Template Page with id: " + templatePageId + " was not found."));
    }
}
