package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.ContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.mapper.LocaleMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContactTypeLocaleMapper {

    public ContactTypeLocaleEntity create(ContactTypeLocaleRequest request) {
        ContactTypeLocaleEntity entity = new ContactTypeLocaleEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ContactTypeLocaleEntity entity, UpdateContactTypeLocaleRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ContactTypeLocaleEntity entity, ContactTypeLocaleRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public ContactTypeLocaleDto toDto(ContactTypeLocaleEntity entity) {
        return ContactTypeLocaleDto.builder()
                .id(entity.getId())
                .locale(LocaleMapper.toDto(entity.getLocaleEntity()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
