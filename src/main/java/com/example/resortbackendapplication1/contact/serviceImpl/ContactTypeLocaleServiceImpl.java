package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.contacttype.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeLocaleDto;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeLocaleMapper;
import com.example.resortbackendapplication1.contact.repository.ContactTypeLocaleRepository;
import com.example.resortbackendapplication1.contact.service.ContactTypeLocaleService;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ContactTypeLocaleServiceImpl implements ContactTypeLocaleService {
    private final ContactTypeLocaleRepository contactTypeLocaleRepository;

    public ContactTypeLocaleServiceImpl(ContactTypeLocaleRepository contactTypeLocaleRepository) {
        this.contactTypeLocaleRepository = contactTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateContactTypeLocaleRequest request,
                                  ContactTypeEntity contactTypeEntity,
                                  LocaleEntity localeEntity) {
        if (contactTypeLocaleRepository.existsByContactTypeEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                contactTypeEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ContactType already has a locale entry for locale id: " + localeEntity.getId());
        }

        ContactTypeLocaleEntity entity = ContactTypeLocaleMapper.create(request);
        contactTypeEntity.addContactTypeLocaleEntity(entity);
        localeEntity.addContactTypeLocaleEntity(entity);
        contactTypeLocaleRepository.save(entity);
        log.info("ContactTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ContactTypeLocaleEntity entity,
                                  UpdateContactTypeLocaleRequest request) {
        ContactTypeLocaleMapper.update(entity, request);
        contactTypeLocaleRepository.save(entity);
        log.info("ContactTypeLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ContactTypeLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        contactTypeLocaleRepository.save(entity);
        log.info("ContactTypeLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ContactTypeLocaleEntity getEntityById(Long contactTypeId, Long id) {
        return contactTypeLocaleRepository
                .findByContactTypeEntity_IdAndIdAndIsActiveAndIsDeleted(contactTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ContactTypeLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ContactTypeLocaleDto> getAll(Long contactTypeId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ContactTypeLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? contactTypeLocaleRepository.findByContactTypeEntity_IdAndIsActiveAndIsDeleted(contactTypeId, true, false, pageable)
                : contactTypeLocaleRepository.findByContactTypeEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        contactTypeId, localeCode, true, false, pageable))
                .map(ContactTypeLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long contactTypeId) {
        List<String> codes = contactTypeLocaleRepository
                .findLocaleEntity_CodeByContactTypeEntity_IdAndIsActiveAndIsDeleted(contactTypeId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
