package com.example.resortbackendapplication1.contact.model.mapper;

import com.example.resortbackendapplication1.contact.dto.request.ContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ContactTypeMapper {

    public ContactTypeEntity create(CreateContactTypeRequest request,
                                    Map<Long, LocaleEntity> localeEntityMap) {
        ContactTypeEntity entity = new ContactTypeEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        entity.setContactTypeLocaleEntities(mapLocales(request.getLocales(), entity, localeEntityMap));
        return entity;
    }

    public void update(ContactTypeEntity entity, UpdateContactTypeRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ContactTypeEntity entity, ContactTypeRequest request) {
        entity.setSortOrder(request.getSortOrder());
    }

    private Set<ContactTypeLocaleEntity> mapLocales(List<CreateContactTypeLocaleRequest> locales,
                                                     ContactTypeEntity entity,
                                                     Map<Long, LocaleEntity> localeEntityMap) {
        if (locales == null || locales.isEmpty()) return new LinkedHashSet<>();
        return locales.stream()
                .map(locale -> ContactTypeLocaleMapper.create(locale, entity, localeEntityMap.get(locale.getLocaleId())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ContactTypeDto toDto(ContactTypeEntity entity) {
        List<ContactTypeLocaleDto> locales = entity.getContactTypeLocaleEntities().stream()
                .map(ContactTypeLocaleMapper::toDto)
                .toList();
        return ContactTypeDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .sortOrder(entity.getSortOrder())
                .locales(locales)
                .build();
    }
}
