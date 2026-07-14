package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.CreateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.ResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.UpdateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserPermissionDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ResortUserPermissionMapper {

    public ResortUserPermissionEntity create(ResortUserPermissionRequest request,
                                             ResortUserEntity resortUserEntity,
                                             ResortPermissionTypeEntity resortPermissionTypeEntity) {
        ResortUserPermissionEntity entity = new ResortUserPermissionEntity();
        entity.setResortUserEntity(resortUserEntity);
        entity.setResortPermissionTypeEntity(resortPermissionTypeEntity);
        entity.setIsAllowed(request.getIsAllowed());
        return entity;
    }

    public List<ResortUserPermissionEntity> createAll(CreateResortUserPermissionRequest request,
                                                      ResortUserEntity resortUserEntity,
                                                      Map<Long, ResortPermissionTypeEntity> resortPermissionTypeEntityMap) {
        return request.getPermissions().stream()
                .map(item -> create(item, resortUserEntity, resortPermissionTypeEntityMap.get(item.getResortPermissionTypeId())))
                .toList();
    }

    public void update(ResortUserPermissionEntity entity, UpdateResortUserPermissionRequest request) {
        entity.setIsAllowed(request.getIsAllowed());
    }

    public ResortUserPermissionDto toDto(ResortUserPermissionEntity entity) {
        return ResortUserPermissionDto.builder()
                .id(entity.getId())
                .resortUserId(entity.getResortUserEntity().getId())
                .resortPermissionTypeId(entity.getResortPermissionTypeEntity().getId())
                .isAllowed(entity.getIsAllowed())
                .build();
    }
}
