package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.CreateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortaddress.locale.UpdateResortAddressLocaleRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortaddresslocales.ResortAddressLocaleCountResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortAddressLocaleDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressLocaleEntity;

public interface ResortAddressLocaleService {

    SuccessResponse create(CreateResortAddressLocaleRequest request,
                           ResortAddressEntity resortAddressEntity,
                           LocaleEntity localeEntity);

    ResortAddressLocaleEntity getEntityById(Long resortAddressId, Long id);

    PaginatedResponse<ResortAddressLocaleDto> getAll(Long resortAddressId, String localeCode, PaginatedRequest paginatedRequest);

    ResortAddressLocaleCountResponse getActiveCount(Long resortAddressId);

    SuccessResponse update(ResortAddressLocaleEntity entity,
                           UpdateResortAddressLocaleRequest request);

    SuccessResponse delete(ResortAddressLocaleEntity entity);
}
