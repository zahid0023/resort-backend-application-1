package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.locale.ContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContactTypeLocaleMapper {

    public ContactTypeLocaleEntity create(CreateContactTypeLocaleRequest request,
                                          ContactTypeEntity contactTypeEntity,
                                          LocaleEntity localeEntity) {
        ContactTypeLocaleEntity entity = new ContactTypeLocaleEntity();
        entity.setContactTypeEntity(contactTypeEntity);
        entity.setLocaleEntity(localeEntity);
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
                .localeId(entity.getLocaleEntity().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
