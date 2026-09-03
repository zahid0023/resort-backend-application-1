package com.example.resortbackendapplication1.mail.provider.model.mapper;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.CreateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.MailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.UpdateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigFieldDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigFieldEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailProviderConfigFieldMapper {

    public MailProviderConfigFieldEntity create(CreateMailProviderConfigFieldRequest request) {
        MailProviderConfigFieldEntity entity = new MailProviderConfigFieldEntity();
        entity.setKey(request.getKey());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(MailProviderConfigFieldEntity entity, UpdateMailProviderConfigFieldRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(MailProviderConfigFieldEntity entity, MailProviderConfigFieldRequest request) {
        entity.setLabel(request.getLabel());
        entity.setFieldType(request.getFieldType());
        entity.setPlaceholder(request.getPlaceholder());
        entity.setDefaultValue(request.getDefaultValue());
        entity.setIsRequired(request.getIsRequired());
        entity.setSortOrder(request.getSortOrder());
    }

    public MailProviderConfigFieldDto.MailProviderConfigFieldDtoBuilder toDto(MailProviderConfigFieldEntity entity) {
        return MailProviderConfigFieldDto.builder()
                .id(entity.getId())
                .key(entity.getKey())
                .label(entity.getLabel())
                .fieldType(entity.getFieldType())
                .placeholder(entity.getPlaceholder())
                .defaultValue(entity.getDefaultValue())
                .isRequired(entity.getIsRequired())
                .sortOrder(entity.getSortOrder());
    }
}
