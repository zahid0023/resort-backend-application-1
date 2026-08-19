package com.example.resortbackendapplication1.resort.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.CreateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortcontacts.ResortContactResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortContactEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;

public interface ResortContactService {

    SuccessResponse create(CreateResortContactRequest request,
                           ResortEntity resortEntity,
                           ContactTypeEntity contactTypeEntity,
                           CommunicationChannelEntity communicationChannelEntity);

    ResortContactEntity getEntityById(Long resortId, Long id);

    ResortContactResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortContactDto> getAll(Long resortId, ResortContactFilterRequest request);

    SuccessResponse update(ResortContactEntity entity,
                           UpdateResortContactRequest request);

    SuccessResponse delete(ResortContactEntity entity);
}
