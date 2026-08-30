package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.userphone.UpdateUserPhoneRequest;
import com.example.resortbackendapplication1.contact.dto.request.userphone.UserPhoneRequest;
import com.example.resortbackendapplication1.contact.model.dto.UserPhoneDto;
import com.example.resortbackendapplication1.contact.model.entity.UserPhoneEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserPhoneMapper {

    public UserPhoneEntity create(UserPhoneRequest request) {
        UserPhoneEntity entity = new UserPhoneEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(UserPhoneEntity entity, UpdateUserPhoneRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UserPhoneEntity entity, UserPhoneRequest request) {
        entity.setPhone(request.getPhone());
        entity.setIsWhatsapp(request.getIsWhatsapp());
        entity.setIsPrimary(request.getIsPrimary());
        entity.setSortOrder(request.getSortOrder());
    }

    public UserPhoneDto toDto(UserPhoneEntity entity) {
        return UserPhoneDto.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .isWhatsapp(entity.getIsWhatsapp())
                .isPrimary(entity.getIsPrimary())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
