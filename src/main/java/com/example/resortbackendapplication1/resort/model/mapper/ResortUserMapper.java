package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class ResortUserMapper {

    public ResortUserEntity create(ResortEntity resortEntity,
                                   UserEntity userEntity,
                                   ResortAccessTypeEntity resortAccessTypeEntity) {
        ResortUserEntity entity = new ResortUserEntity();
        entity.setResortEntity(resortEntity);
        entity.setUserEntity(userEntity);
        applyCommonFields(entity, resortAccessTypeEntity);
        return entity;
    }

    public void update(ResortUserEntity entity, ResortAccessTypeEntity resortAccessTypeEntity) {
        applyCommonFields(entity, resortAccessTypeEntity);
    }

    private void applyCommonFields(ResortUserEntity entity, ResortAccessTypeEntity resortAccessTypeEntity) {
        entity.setResortAccessTypeEntity(resortAccessTypeEntity);
        entity.setJoinedAt(Instant.now());
    }

    public ResortUserDto toDto(ResortUserEntity entity) {
        return ResortUserDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .userId(entity.getUserEntity().getId())
                .resortAccessTypeId(entity.getResortAccessTypeEntity().getId())
                .joinedAt(entity.getJoinedAt())
                .build();
    }
}
