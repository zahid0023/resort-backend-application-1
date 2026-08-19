package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resort.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.resorts.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;

public interface ResortService {

    /**
     * Creates a resort, makes {@code userEntity} its OWNER with ALL_PERMISSIONS, and creates its
     * ResortBasicInfo and ResortAddress in the same transaction.
     *
     * @param resortRoleTypeEntity       the OWNER resort role type, granted to {@code userEntity}
     * @param resortPermissionTypeEntity the ALL_PERMISSIONS resort permission type, granted to {@code userEntity}
     * @param countryEntity              country for the resort's address (request.getAddress().getCountryId())
     * @param cityEntity                 city for the resort's address (request.getAddress().getCityId())
     * @param localeEntity               the {@code en} locale, used for the basic-info and address translations
     */
    SuccessResponse create(CreateResortRequest request,
                           UserEntity userEntity,
                           ResortRoleTypeEntity resortRoleTypeEntity,
                           ResortPermissionTypeEntity resortPermissionTypeEntity,
                           CountryEntity countryEntity,
                           CityEntity cityEntity,
                           LocaleEntity localeEntity);

    ResortEntity getEntityById(Long id);

    ResortResponse getById(Long id);

    PaginatedResponse<ResortDto> getAll(ResortFilterRequest request);

    PaginatedResponse<ResortDto> getMyResorts(Long authenticatedUserId, PaginatedRequest request);

    SuccessResponse update(ResortEntity entity,
                           UpdateResortRequest request);

    SuccessResponse delete(ResortEntity entity);
}
