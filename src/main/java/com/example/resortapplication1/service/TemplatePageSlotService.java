package com.example.resortapplication1.service;

import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.templatepageslots.CreateTemplatePageSlotRequest;
import com.example.resortapplication1.dto.request.templatepageslots.UpdateTemplatePageSlotRequest;
import com.example.resortapplication1.dto.response.templatepageslots.TemplatePageSlotResponse;
import com.example.resortapplication1.model.dto.TemplatePageSlotDto;
import com.example.resortapplication1.model.entity.TemplatePageEntity;
import com.example.resortapplication1.model.entity.TemplatePageSlotEntity;
import com.example.resortapplication1.model.entity.UiBlockCategoryEntity;
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
