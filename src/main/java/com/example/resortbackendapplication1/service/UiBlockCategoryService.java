package com.example.resortbackendapplication1.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.uiblockcategories.CreateUiBlockCategoryRequest;
import com.example.resortbackendapplication1.dto.request.uiblockcategories.UpdateUiBlockCategoryRequest;
import com.example.resortbackendapplication1.dto.response.uiblockcategories.UiBlockCategoryResponse;
import com.example.resortbackendapplication1.model.dto.UiBlockCategoryDto;
import com.example.resortbackendapplication1.model.entity.UiBlockCategoryEntity;
import org.springframework.data.domain.Pageable;

public interface UiBlockCategoryService {
    SuccessResponse createUiBlockCategory(CreateUiBlockCategoryRequest request);

    UiBlockCategoryEntity getUiBlockCategoryById(Long id);

    UiBlockCategoryResponse getUiBlockCategory(Long id);

    PaginatedResponse<UiBlockCategoryDto> getAllUiBlockCategories(Pageable pageable);

    UiBlockCategoryResponse updateUiBlockCategory(Long id, UpdateUiBlockCategoryRequest request);

    SuccessResponse deleteUiBlockCategory(Long id);
}
