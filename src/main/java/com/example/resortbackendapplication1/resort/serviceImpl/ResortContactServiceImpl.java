package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.mapper.CommunicationChannelMapper;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeMapper;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.CreateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortcontact.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resort.dto.response.resortcontacts.ResortContactResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortContactEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortContactSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortContactSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortContactMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.repository.ResortContactRepository;
import com.example.resortbackendapplication1.resort.service.ResortContactService;
import com.example.resortbackendapplication1.resort.specification.ResortContactSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortContactServiceImpl implements ResortContactService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortContactSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortContactSearchField.allowedFields();

    private final ResortContactRepository resortContactRepository;

    public ResortContactServiceImpl(ResortContactRepository resortContactRepository) {
        this.resortContactRepository = resortContactRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortContactRequest request,
                                  ResortEntity resortEntity,
                                  ContactTypeEntity contactTypeEntity,
                                  CommunicationChannelEntity communicationChannelEntity) {
        if (resortContactRepository.existsByResortEntity_IdAndContactTypeEntity_IdAndCommunicationChannelEntity_IdAndContactValueAndIsActiveAndIsDeleted(
                resortEntity.getId(), contactTypeEntity.getId(), communicationChannelEntity.getId(),
                request.getContactValue(), true, false)) {
            throw new IllegalStateException(
                    "ResortContact with this contact type, communication channel, and value already exists for this resort");
        }

        ResortContactEntity entity = ResortContactMapper.create(request);
        resortEntity.addResortContactEntity(entity);
        entity.setContactTypeEntity(contactTypeEntity);
        entity.setCommunicationChannelEntity(communicationChannelEntity);

        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetExistingPrimary(resortEntity.getId(), contactTypeEntity.getId(), communicationChannelEntity.getId());
        }

        resortContactRepository.save(entity);
        log.info("ResortContact created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortContactEntity getEntityById(Long resortId, Long id) {
        return resortContactRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortContact not found with id: " + id));
    }

    @Override
    public ResortContactResponse getById(Long resortId, Long id) {
        ResortContactEntity entity = getEntityById(resortId, id);
        return new ResortContactResponse(buildDto(entity));
    }

    @Override
    public PaginatedResponse<ResortContactDto> getAll(Long resortId, ResortContactFilterRequest request) {
        Specification<@NonNull ResortContactEntity> specification =
                ResortContactSpecification.filter(request, resortId);
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortContactSortField.localeSortFields());
        Page<@NonNull ResortContactDto> page = resortContactRepository
                .findAll(specification, pageable)
                .map(this::buildDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortContactEntity entity, UpdateResortContactRequest request) {
        if (Boolean.TRUE.equals(request.getIsPrimary()) && !Boolean.TRUE.equals(entity.getIsPrimary())) {
            unsetExistingPrimary(entity.getResortEntity().getId(), entity.getContactTypeEntity().getId(),
                    entity.getCommunicationChannelEntity().getId());
        }

        ResortContactMapper.update(entity, request);
        resortContactRepository.save(entity);
        log.info("ResortContact updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortContactEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortContactRepository.save(entity);
        log.info("ResortContact soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void unsetExistingPrimary(Long resortId, Long contactTypeId, Long communicationChannelId) {
        resortContactRepository
                .findByResortEntity_IdAndContactTypeEntity_IdAndCommunicationChannelEntity_IdAndIsPrimaryTrueAndIsActiveAndIsDeleted(
                        resortId, contactTypeId, communicationChannelId, true, false)
                .ifPresent(existing -> {
                    existing.setIsPrimary(false);
                    resortContactRepository.save(existing);
                });
    }

    private ResortContactDto buildDto(ResortContactEntity entity) {
        ResortDto resort = ResortMapper.toDto(entity.getResortEntity()).build();
        ContactTypeDto contactType = ContactTypeMapper.toDto(entity.getContactTypeEntity()).build();
        CommunicationChannelDto communicationChannel = CommunicationChannelMapper.toDto(entity.getCommunicationChannelEntity()).build();
        return ResortContactMapper.toDto(entity)
                .resort(resort)
                .contactType(contactType)
                .communicationChannel(communicationChannel)
                .build();
    }
}
