package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.CreateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.communicationchannel.locale.UpdateCommunicationChannelLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelLocaleEntity;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelLocaleMapper;
import com.example.resortbackendapplication1.contact.repository.CommunicationChannelLocaleRepository;
import com.example.resortbackendapplication1.contact.service.CommunicationChannelLocaleService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class CommunicationChannelLocaleServiceImpl implements CommunicationChannelLocaleService {
    private final CommunicationChannelLocaleRepository communicationChannelLocaleRepository;

    public CommunicationChannelLocaleServiceImpl(CommunicationChannelLocaleRepository communicationChannelLocaleRepository) {
        this.communicationChannelLocaleRepository = communicationChannelLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateCommunicationChannelLocaleRequest request,
                                  CommunicationChannelEntity communicationChannelEntity,
                                  LocaleEntity localeEntity) {
        if (communicationChannelLocaleRepository.existsByCommunicationChannelEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                communicationChannelEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("CommunicationChannel already has a locale entry for locale id: " + localeEntity.getId());
        }

        CommunicationChannelLocaleEntity entity = CommunicationChannelLocaleMapper.create(request);
        communicationChannelEntity.addCommunicationChannelLocaleEntity(entity);
        localeEntity.addCommunicationChannelLocaleEntity(entity);
        communicationChannelLocaleRepository.save(entity);
        log.info("CommunicationChannelLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
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

    @Override
    public CommunicationChannelLocaleEntity getEntityById(Long communicationChannelId, Long id) {
        return communicationChannelLocaleRepository
                .findByCommunicationChannelEntity_IdAndIdAndIsActiveAndIsDeleted(communicationChannelId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("CommunicationChannelLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<CommunicationChannelLocaleDto> getAll(Long communicationChannelId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull CommunicationChannelLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? communicationChannelLocaleRepository.findByCommunicationChannelEntity_IdAndIsActiveAndIsDeleted(communicationChannelId, true, false, pageable)
                : communicationChannelLocaleRepository.findByCommunicationChannelEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        communicationChannelId, localeCode, true, false, pageable))
                .map(CommunicationChannelLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }
}
