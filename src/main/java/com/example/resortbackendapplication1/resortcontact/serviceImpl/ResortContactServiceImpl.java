package com.example.resortbackendapplication1.resortcontact.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.model.entity.CommunicationChannelEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resortcontact.dto.request.CreateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.ResortContactFilterRequest;
import com.example.resortbackendapplication1.resortcontact.dto.request.UpdateResortContactRequest;
import com.example.resortbackendapplication1.resortcontact.dto.response.ResortContactResponse;
import com.example.resortbackendapplication1.resortcontact.model.dto.ResortContactDto;
import com.example.resortbackendapplication1.resortcontact.model.entity.ResortContactEntity;
import com.example.resortbackendapplication1.resortcontact.model.enums.ResortContactSearchField;
import com.example.resortbackendapplication1.resortcontact.model.enums.ResortContactSortField;
import com.example.resortbackendapplication1.resortcontact.model.mapper.ResortContactMapper;
import com.example.resortbackendapplication1.resortcontact.repository.ResortContactRepository;
import com.example.resortbackendapplication1.resortcontact.service.ResortContactService;
import com.example.resortbackendapplication1.resortcontact.specification.ResortContactSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
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
    public SuccessResponse create(ResortEntity resort,
                                  ContactTypeEntity contactType,
                                  CommunicationChannelEntity communicationChannel,
                                  CreateResortContactRequest request) {
        ResortContactEntity entity = ResortContactMapper.create(request, resort, contactType, communicationChannel);
        resortContactRepository.save(entity);
        log.info("ResortContact created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortContactEntity getEntityById(Long resortId, Long id) {
        return resortContactRepository
                .findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(id, resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortContact not found with id: " + id));
    }

    @Override
    public ResortContactResponse getById(Long resortId, Long id) {
        ResortContactEntity entity = getEntityById(resortId, id);
        return new ResortContactResponse(ResortContactMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortContactDto> getAll(ResortContactFilterRequest request, Long resortId) {
        Page<@NonNull ResortContactDto> page = resortContactRepository
                .findAll(ResortContactSpecification.filter(request, resortId), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortContactMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortContactEntity entity, UpdateResortContactRequest request) {
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
}
