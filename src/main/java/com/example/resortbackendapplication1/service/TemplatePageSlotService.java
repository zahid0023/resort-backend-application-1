package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.templatepageslots.CreateTemplatePageSlotRequest;
import com.example.resortbackendapplication1.dto.request.templatepageslots.UpdateTemplatePageSlotRequest;
import com.example.resortbackendapplication1.dto.response.templatepageslots.TemplatePageSlotResponse;
import com.example.resortbackendapplication1.model.dto.TemplatePageSlotDto;
import com.example.resortbackendapplication1.model.entity.TemplatePageEntity;
import com.example.resortbackendapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortbackendapplication1.model.entity.UiBlockCategoryEntity;
import org.springframework.data.domain.Pageable;

public interface TemplatePageSlotService {
    SuccessResponse createTemplatePageSlot(CreateTemplatePageSlotRequest request,
                                           TemplatePageEntity templatePageEntity,
                                           UiBlockCategoryEntity uiBlockCategoryEntity);

    TemplatePageSlotEntity getTemplatePageSlotEntity(Long templateId, Long templatePageId, Long templatePageSlotId);

    TemplatePageSlotResponse getTemplatePageSlot(Long templateId, Long templatePageId, Long templatePageSlotId);

    PaginatedResponse<TemplatePageSlotDto> getAllTemplatePageSlots(Long templateId, Long templatePageId, Pageable pageable);

    SuccessResponse updateTemplatePageSlot(UpdateTemplatePageSlotRequest request,
                                                    TemplatePageSlotEntity templatePageSlotEntity,
                                                    UiBlockCategoryEntity uiBlockCategoryEntity);

    SuccessResponse deleteTemplatePageSlot(Long templateId, Long templatePageId, Long templatePageSlotId);
}
