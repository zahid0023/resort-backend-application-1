package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.ContactTypeFilterRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.CreateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.UpdateContactTypeRequest;
import com.example.resortbackendapplication1.contact.dto.response.contacttypes.ContactTypeResponse;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.contact.model.enums.ContactTypeSearchField;
import com.example.resortbackendapplication1.contact.model.enums.ContactTypeSortField;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeLocaleMapper;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeMapper;
import com.example.resortbackendapplication1.contact.repository.ContactTypeRepository;
import com.example.resortbackendapplication1.contact.service.ContactTypeService;
import com.example.resortbackendapplication1.contact.specification.ContactTypeSpecification;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
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
public class ContactTypeServiceImpl implements ContactTypeService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ContactTypeSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ContactTypeSearchField.allowedFields();

    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeServiceImpl(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateContactTypeRequest request, LocaleEntity localeEntity) {
        if (contactTypeRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("ContactType with code '" + request.getCode() + "' already exists");
        }

        ContactTypeEntity entity = ContactTypeMapper.create(request);

        ContactTypeLocaleEntity contactTypeLocaleEntity = ContactTypeLocaleMapper.create(request.getLocale());
        localeEntity.addContactTypeLocaleEntity(contactTypeLocaleEntity);

        entity.addContactTypeLocaleEntity(contactTypeLocaleEntity);

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
        ContactTypeDto dto = ContactTypeMapper.toDto(entity).build();
        return new ContactTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<ContactTypeDto> getAll(ContactTypeFilterRequest request) {
        Specification<@NonNull ContactTypeEntity> specification = ContactTypeSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ContactTypeSortField.localeSortFields());
        Page<@NonNull ContactTypeDto> page = contactTypeRepository
                .findAll(specification, pageable)
                .map(entity -> ContactTypeMapper.toDto(entity).build());
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
    public SuccessResponse delete(ContactTypeEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getContactTypeLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        contactTypeRepository.save(entity);
        log.info("ContactType soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
