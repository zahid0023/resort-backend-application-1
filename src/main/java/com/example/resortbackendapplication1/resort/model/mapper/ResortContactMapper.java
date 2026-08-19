package com.example.resortbackendapplication1.resort.model.mapper;

import com.example.resortbackendapplication1.resort.dto.request.resortcontact.CreateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.ResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resort.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortContactEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortContactMapper {

    public ResortContactEntity create(CreateResortContactRequest request) {
        ResortContactEntity entity = new ResortContactEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortContactEntity entity, UpdateResortContactRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortContactEntity entity, ResortContactRequest request) {
        entity.setContactValue(request.getContactValue());
        entity.setIsPrimary(request.getIsPrimary());
        entity.setSortOrder(request.getSortOrder());
    }

    public ResortContactDto.ResortContactDtoBuilder toDto(ResortContactEntity entity) {
        return ResortContactDto.builder()
                .id(entity.getId())
                .contactValue(entity.getContactValue())
                .isPrimary(entity.getIsPrimary())
                .sortOrder(entity.getSortOrder());
    }
}
