package com.example.resortbackendapplication1.resort.mail.service;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.CreateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.ResortMailConfigFilterRequest;
import com.example.resortbackendapplication1.resort.mail.dto.request.resortmailconfig.UpdateResortMailConfigRequest;
import com.example.resortbackendapplication1.resort.mail.dto.response.resortmailconfigs.ResortMailConfigResponse;
import com.example.resortbackendapplication1.resort.mail.model.dto.ResortMailConfigDto;
import com.example.resortbackendapplication1.resort.mail.model.entity.ResortMailConfigEntity;

public interface ResortMailConfigService {

    SuccessResponse create(CreateResortMailConfigRequest request,
                           ResortEntity resortEntity,
                           MailProviderEntity mailProviderEntity);

    ResortMailConfigEntity getEntityById(Long resortId, Long id);

    ResortMailConfigResponse getById(Long resortId, Long id);

    PaginatedResponse<ResortMailConfigDto> getAll(Long resortId, ResortMailConfigFilterRequest request);

    SuccessResponse update(ResortMailConfigEntity entity, UpdateResortMailConfigRequest request);

    SuccessResponse delete(ResortMailConfigEntity entity);
}
