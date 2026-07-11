package com.example.resortbackendapplication1.resortcontact.model.mapper;

import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelMapper;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeMapper;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.ResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResortContactMapper {

    public ResortContactEntity create(CreateResortContactRequest request,
                                      ResortEntity resort,
                                      ContactTypeEntity contactType,
                                      CommunicationChannelEntity communicationChannel) {
        ResortContactEntity entity = new ResortContactEntity();
        entity.setResortEntity(resort);
        entity.setContactTypeEntity(contactType);
        entity.setCommunicationChannelEntity(communicationChannel);
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

    public ResortContactDto toDto(ResortContactEntity entity) {
        return ResortContactDto.builder()
                .id(entity.getId())
                .resortId(entity.getResortEntity().getId())
                .contactType(ContactTypeMapper.toDto(entity.getContactTypeEntity()))
                .communicationChannel(CommunicationChannelMapper.toDto(entity.getCommunicationChannelEntity()))
                .contactValue(entity.getContactValue())
                .isPrimary(entity.getIsPrimary())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
