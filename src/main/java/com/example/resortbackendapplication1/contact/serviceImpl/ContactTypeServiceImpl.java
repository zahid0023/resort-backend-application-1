package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.EntityValidator;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.dto.request.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.response.ContactTypeResponse;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.enums.ContactTypeSearchField;
import com.example.resortbackendapplication1.contact.model.enums.ContactTypeSortField;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeMapper;
import com.example.resortbackendapplication1.contact.repository.ContactTypeRepository;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.contact.specification.ContactTypeSpecification;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ContactTypeServiceImpl implements ContactTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ContactTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ContactTypeSearchField.allowedFields();

    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeServiceImpl(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateContactTypeRequest request, Map<Long, LocaleEntity> localeEntityMap) {
        ContactTypeEntity entity = ContactTypeMapper.create(request, localeEntityMap);
        contactTypeRepository.save(entity);
        log.info("ContactType created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ContactTypeEntity getEntityById(Long id) {
        return contactTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ContactType not found with id: " + id));
    }

    @Override
    public ContactTypeResponse getById(Long id) {
        ContactTypeEntity entity = getEntityById(id);
        return new ContactTypeResponse(ContactTypeMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ContactTypeDto> getAll(ContactTypeFilterRequest request) {
        Page<@NonNull ContactTypeDto> page = contactTypeRepository
                .findAll(ContactTypeSpecification.filter(request), request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ContactTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ContactTypeEntity entity, UpdateContactTypeRequest request) {
        ContactTypeMapper.update(entity, request);
        contactTypeRepository.save(entity);
        log.info("ContactType updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(Long id) {
        ContactTypeEntity entity = getEntityById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        contactTypeRepository.save(entity);
        log.info("ContactType soft-deleted with id: {}", id);
        return new SuccessResponse(true, id);
    }

    @Override
    public List<ContactTypeEntity> getAll(Set<Long> ids) {
        List<ContactTypeEntity> entities = contactTypeRepository
                .findAllByIdInAndIsActiveAndIsDeleted(ids, true, false);
        EntityValidator.validateAllFound(ids, entities, ContactTypeEntity::getId, "ContactType");
        return entities;
    }
}
