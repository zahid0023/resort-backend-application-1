package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelLocaleMapper;
import com.example.resortbackendapplication1.contact.repository.CommunicationChannelLocaleRepository;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CommunicationChannelLocaleServiceImpl implements CommunicationChannelLocaleService {

    private final CommunicationChannelLocaleRepository communicationChannelLocaleRepository;

    public CommunicationChannelLocaleServiceImpl(
            CommunicationChannelLocaleRepository communicationChannelLocaleRepository) {
        this.communicationChannelLocaleRepository = communicationChannelLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CommunicationChannelEntity channelEntity,
                                  LocaleEntity localeEntity,
                                  CreateCommunicationChannelLocaleRequest request) {
        CommunicationChannelLocaleEntity entity =
                CommunicationChannelLocaleMapper.create(request, channelEntity, localeEntity);
        communicationChannelLocaleRepository.save(entity);
        log.info("CommunicationChannelLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CommunicationChannelLocaleEntity getEntityById(Long channelId, Long id) {
        return communicationChannelLocaleRepository
                .findByCommunicationChannelEntity_IdAndIdAndIsActiveAndIsDeleted(channelId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CommunicationChannelLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(CommunicationChannelLocaleEntity entity,
                                  UpdateCommunicationChannelLocaleRequest request) {
        CommunicationChannelLocaleMapper.update(entity, request);
        communicationChannelLocaleRepository.save(entity);
        log.info("CommunicationChannelLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(CommunicationChannelLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        communicationChannelLocaleRepository.save(entity);
        log.info("CommunicationChannelLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
