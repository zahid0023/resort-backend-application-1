package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.templatepageslotvariants.CreateTemplatePageSlotVariantRequest;
import com.example.resortbackendapplication1.dto.request.templatepageslotvariants.UpdateTemplatePageSlotVariantRequest;
import com.example.resortbackendapplication1.dto.response.templatepageslotvariants.TemplatePageSlotVariantResponse;
import com.example.resortbackendapplication1.model.dto.TemplatePageSlotVariantDto;
import com.example.resortbackendapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortbackendapplication1.model.entity.TemplatePageSlotVariantEntity;
import com.example.resortbackendapplication1.model.entity.UiBlockDefinitionEntity;
import org.springframework.data.domain.Pageable;

public interface TemplatePageSlotVariantService {
    SuccessResponse createTemplatePageSlotVariant(CreateTemplatePageSlotVariantRequest request,
                                                  TemplatePageSlotEntity templatePageSlotEntity,
                                                  UiBlockDefinitionEntity uiBlockDefinitionEntity);

    TemplatePageSlotVariantEntity getTemplatePageSlotVariantEntity(Long templateId, Long templatePageId,
                                                                   Long templatePageSlotId, Long id);

    TemplatePageSlotVariantResponse getTemplatePageSlotVariant(Long templateId, Long templatePageId,
                                                               Long templatePageSlotId, Long id);

    PaginatedResponse<TemplatePageSlotVariantDto> getAllTemplatePageSlotVariants(Long templateId, Long templatePageId,
                                                                                 Long templatePageSlotId, Pageable pageable);

    SuccessResponse updateTemplatePageSlotVariant(UpdateTemplatePageSlotVariantRequest request,
                                                                  TemplatePageSlotVariantEntity templatePageSlotVariantEntity,
                                                                  UiBlockDefinitionEntity uiBlockDefinitionEntity);

    SuccessResponse deleteTemplatePageSlotVariant(Long templateId, Long templatePageId,
                                                  Long templatePageSlotId, Long id);
}
