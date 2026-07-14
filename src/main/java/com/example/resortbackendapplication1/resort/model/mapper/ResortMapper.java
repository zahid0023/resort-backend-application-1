package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.ResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.ResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resortbasicinfo.model.mapper.ResortBasicInfoMapper;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import com.example.resortbackendapplication1.resortcontact.model.mapper.ResortContactMapper;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ResortMapper {

    public ResortEntity create(CreateResortRequest request,
                               UserEntity userEntity,
                               ResortAccessTypeEntity resortAccessTypeEntity,
                               ResortPermissionTypeEntity resortPermissionTypeEntity,
                               CountryEntity countryEntity,
                               CityEntity cityEntity,
                               Map<Long, LocaleEntity> localeEntityMap,
                               Map<Long, ContactTypeEntity> contactTypeEntityMap,
                               Map<Long, CommunicationChannelEntity> communicationChannelEntityMap) {
        ResortEntity resortEntity = new ResortEntity();
        resortEntity.setCode(request.getCode());
        applyCommonFields(resortEntity, request);

        ResortUserEntity resortUserEntity = ResortUserMapper.create(resortEntity, userEntity, resortAccessTypeEntity);
        ResortUserPermissionEntity resortUserPermissionEntity = ResortUserPermissionMapper.create(
                ResortUserPermissionRequest.builder()
                        .resortPermissionTypeId(resortPermissionTypeEntity.getId())
                        .isAllowed(true)
                        .build(),
                resortUserEntity,
                resortPermissionTypeEntity);

        resortUserEntity.setResortUserPermissionEntities(Set.of(resortUserPermissionEntity));

        resortEntity.setResortUserEntities(Set.of(resortUserEntity));

        ResortBasicInfoEntity resortBasicInfoEntity = ResortBasicInfoMapper.create(request.getBasicInfo(),
                resortEntity, countryEntity, cityEntity, localeEntityMap);
        resortEntity.setResortBasicInfoEntity(resortBasicInfoEntity);

        Set<ResortContactEntity> resortContactEntities = request.getContacts().stream()
                .map(createResortContactRequest -> ResortContactMapper.create(
                        createResortContactRequest,
                        resortEntity,
                        contactTypeEntityMap.get(createResortContactRequest.getContactTypeId()),
                        communicationChannelEntityMap.get(createResortContactRequest.getCommunicationChannelId())))
                .collect(Collectors.toSet());
        resortEntity.setResortContactEntities(resortContactEntities);

        return resortEntity;
    }

    public void update(ResortEntity entity, UpdateResortRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortEntity entity, ResortRequest request) {
        // shared mutable fields applied here as they are added
    }

    public ResortDto toDto(ResortEntity entity) {
        return ResortDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .build();
    }

    public ResortDto toSummaryDto(ResortEntity entity) {
        ResortBasicInfoEntity rbi = entity.getResortBasicInfoEntity();
        return ResortDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .resortBasicInfo(rbi != null ? ResortBasicInfoMapper.toDto(rbi) : null)
                .build();
    }
}
