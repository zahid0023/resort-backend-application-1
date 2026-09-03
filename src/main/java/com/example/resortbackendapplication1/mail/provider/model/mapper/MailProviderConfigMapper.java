package com.example.resortbackendapplication1.mail.provider.model.mapper;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.CreateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.MailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.config.UpdateMailProviderConfigRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailProviderConfigMapper {

    public MailProviderConfigEntity create(CreateMailProviderConfigRequest request) {
        MailProviderConfigEntity entity = new MailProviderConfigEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(MailProviderConfigEntity entity, UpdateMailProviderConfigRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(MailProviderConfigEntity entity, MailProviderConfigRequest request) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setConfig(request.getConfig());
    }

    public MailProviderConfigDto.MailProviderConfigDtoBuilder toDto(MailProviderConfigEntity entity) {
        return MailProviderConfigDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .config(entity.getConfig());
    }
}
