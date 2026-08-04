package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.ContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ContactTypeMapper {

    public ContactTypeEntity create(CreateContactTypeRequest request) {
        ContactTypeEntity entity = new ContactTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ContactTypeEntity entity, UpdateContactTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ContactTypeEntity entity, ContactTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    public ContactTypeDto.ContactTypeDtoBuilder toDto(ContactTypeEntity entity) {
        return ContactTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locale(singleLocale(entity));
    }

    private List<ContactTypeLocaleEntity> activeLocales(ContactTypeEntity entity) {
        return entity.getContactTypeLocaleEntities().stream()
                .filter(contactTypeLocaleEntity -> Boolean.TRUE.equals(contactTypeLocaleEntity.getIsActive())
                        && Boolean.FALSE.equals(contactTypeLocaleEntity.getIsDeleted()))
                .toList();
    }

    private ContactTypeLocaleDto singleLocale(ContactTypeEntity entity) {
        ContactTypeLocaleEntity matched = matchLocale(entity, LocaleContext.getLocaleId());
        return matched == null ? null : ContactTypeLocaleMapper.toDto(matched);
    }

    private ContactTypeLocaleEntity matchLocale(ContactTypeEntity entity, Long localeId) {
        List<ContactTypeLocaleEntity> activeLocales = activeLocales(entity);
        return activeLocales.stream()
                .filter(contactTypeLocaleEntity -> contactTypeLocaleEntity.getLocaleEntity().getId().equals(localeId))
                .findFirst()
                .orElseGet(() -> activeLocales.stream()
                        .filter(contactTypeLocaleEntity -> "en".equals(contactTypeLocaleEntity.getLocaleEntity().getCode()))
                        .findFirst()
                        .orElse(null));
    }
}
