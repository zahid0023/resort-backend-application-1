package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.uiblockdefinitions.CreateUiBlockDefinitionRequest;
import com.example.resortbackendapplication1.dto.request.uiblockdefinitions.UpdateUiBlockDefinitionRequest;
import com.example.resortbackendapplication1.dto.response.uiblockdefinitions.UiBlockDefinitionResponse;
import com.example.resortbackendapplication1.model.dto.UiBlockDefinitionDto;
import com.example.resortbackendapplication1.model.entity.UiBlockDefinitionEntity;
import org.springframework.data.domain.Pageable;

public interface UiBlockDefinitionService {
    SuccessResponse createUiBlockDefinition(CreateUiBlockDefinitionRequest request);

    UiBlockDefinitionEntity getUiBlockDefinitionById(Long id);

    UiBlockDefinitionResponse getUiBlockDefinition(Long id);

    PaginatedResponse<UiBlockDefinitionDto> getAllUiBlockDefinitions(Pageable pageable);

    UiBlockDefinitionResponse updateUiBlockDefinition(Long id, UpdateUiBlockDefinitionRequest request);

    SuccessResponse deleteUiBlockDefinition(Long id);
}
