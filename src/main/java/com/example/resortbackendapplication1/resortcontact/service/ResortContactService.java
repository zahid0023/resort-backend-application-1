package com.example.resortbackendapplication1.resortcontact.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.response.ResortContactResponse;
import com.example.resortbackendapplication1.resortcontact.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;

public interface ResortContactService {

    SuccessResponse create(ResortEntity resort,
                          ContactTypeEntity contactType,
                          CommunicationChannelEntity communicationChannel,
                          CreateResortContactRequest request);

    ResortContactEntity getEntityById(Long resortId, Long id);

    ResortContactResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortContactDto> getAll(ResortContactFilterRequest request, Long resortId);

    SuccessResponse update(ResortContactEntity entity, UpdateResortContactRequest request);

    SuccessResponse delete(ResortContactEntity entity);
}
