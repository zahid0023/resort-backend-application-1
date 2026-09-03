package com.example.resortbackendapplication1.mail.provider.model.mapper;

import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.CreateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.MailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.UpdateMailProviderRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailProviderMapper {

    public MailProviderEntity create(CreateMailProviderRequest request) {
        MailProviderEntity entity = new MailProviderEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(MailProviderEntity entity, UpdateMailProviderRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(MailProviderEntity entity, MailProviderRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public MailProviderDto.MailProviderDtoBuilder toDto(MailProviderEntity entity) {
        return MailProviderDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder());
    }
}
