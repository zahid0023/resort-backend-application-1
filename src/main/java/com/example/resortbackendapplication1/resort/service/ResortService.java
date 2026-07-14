package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResortService {

    SuccessResponse create(CreateResortRequest request,
                           UserEntity userEntity,
                           ResortAccessTypeEntity resortAccessTypeEntity,
                           ResortPermissionTypeEntity resortPermissionTypeEntity,
                           CountryEntity countryEntity,
                           CityEntity cityEntity,
                           Map<Long, LocaleEntity> localeEntityMap,
                           Map<Long, ContactTypeEntity> contactTypeEntityMap,
                           Map<Long, CommunicationChannelEntity> communicationChannelEntityMap);

    ResortEntity getEntityById(Long id);

    ResortResponse getById(Long id);

    PaginatedResponse<ResortDto> getAll(ResortFilterRequest request);

    PaginatedResponse<ResortDto> getAllForUser(ResortFilterRequest request, Long userId);

    SuccessResponse update(ResortEntity entity, UpdateResortRequest request);

    SuccessResponse delete(Long id);

    List<ResortEntity> getAll(Set<Long> ids);
}
