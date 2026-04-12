package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.templatepages.CreateTemplatePageRequest;
import com.example.resortbackendapplication1.dto.request.templatepages.UpdateTemplatePageRequest;
import com.example.resortbackendapplication1.dto.response.templatepages.TemplatePageResponse;
import com.example.resortbackendapplication1.model.dto.TemplatePageDto;
import com.example.resortbackendapplication1.model.entity.TemplateEntity;
import com.example.resortbackendapplication1.model.entity.TemplatePageEntity;
import org.springframework.data.domain.Pageable;

public interface TemplatePageService {
    SuccessResponse createTemplatePage(CreateTemplatePageRequest request, TemplateEntity templateEntity);

    TemplatePageEntity getTemplatePageEntity(Long templateId, Long templatePageId);

    TemplatePageResponse getTemplatePage(Long templateId, Long templatePageId);

    PaginatedResponse<TemplatePageDto> getAllTemplatePages(Long templateId, Pageable pageable);

    TemplatePageResponse updateTemplatePage(UpdateTemplatePageRequest request, TemplatePageEntity templatePageEntity);

    SuccessResponse deleteTemplatePage(Long templateId, Long templatePageId);
}
