package com.example.resortbackendapplication1.resort.mail.model.mapper;

import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.CreateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.UpdateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.model.dto.ResortMailConfigDto;
import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortMailConfigMapper {

    public ResortMailConfigEntity create(CreateResortMailConfigRequest request) {
        ResortMailConfigEntity entity = new ResortMailConfigEntity();
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(ResortMailConfigEntity entity, UpdateResortMailConfigRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(ResortMailConfigEntity entity, ResortMailConfigRequest request) {
        entity.setName(request.getName());
        entity.setConfig(request.getConfig());
    }

    public ResortMailConfigDto.ResortMailConfigDtoBuilder toDto(ResortMailConfigEntity entity) {
        return ResortMailConfigDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .config(entity.getConfig());
    }
}
