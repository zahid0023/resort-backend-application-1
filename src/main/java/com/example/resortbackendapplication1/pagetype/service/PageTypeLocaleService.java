package com.example.resortbackendapplication1.pagetype.service;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.CreatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.dto.request.pagetype.pagetypelocale.UpdatePageTypeLocaleRequest;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeLocaleEntity;

public interface PageTypeLocaleService {

    SuccessResponse create(PageTypeEntity pageTypeEntity,
                           LocaleEntity localeEntity,
                           CreatePageTypeLocaleRequest request);

    PageTypeLocaleEntity getEntityById(Long pageTypeId, Long id);

    SuccessResponse update(PageTypeLocaleEntity entity,
                           UpdatePageTypeLocaleRequest request);

    SuccessResponse delete(PageTypeLocaleEntity entity);
}
