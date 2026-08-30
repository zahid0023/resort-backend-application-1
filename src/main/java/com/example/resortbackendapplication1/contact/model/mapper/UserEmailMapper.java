package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.useremail.UpdateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.request.useremail.UserEmailRequest;
import com.example.resortbackendapplication1.contact.model.dto.UserEmailDto;
import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEmailMapper {

    public UserEmailEntity create(UserEmailRequest request) {
        UserEmailEntity entity = new UserEmailEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(UserEmailEntity entity, UpdateUserEmailRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(UserEmailEntity entity, UserEmailRequest request) {
        entity.setEmail(request.getEmail());
        entity.setIsPrimary(request.getIsPrimary());
        entity.setSortOrder(request.getSortOrder());
    }

    public UserEmailDto toDto(UserEmailEntity entity) {
        return UserEmailDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .isPrimary(entity.getIsPrimary())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
