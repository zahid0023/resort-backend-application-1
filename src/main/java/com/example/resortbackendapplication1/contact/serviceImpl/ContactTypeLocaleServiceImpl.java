package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.contact.dto.request.locale.CreateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.dto.request.locale.UpdateContactTypeLocaleRequest;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import com.example.resortbackendapplication1.contact.model.mapper.ContactTypeLocaleMapper;
import com.example.resortbackendapplication1.contact.repository.ContactTypeLocaleRepository;
import com.example.resortbackendapplication1.contact.service.ContactTypeLocaleService;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ContactTypeLocaleServiceImpl implements ContactTypeLocaleService {

    private final ContactTypeLocaleRepository contactTypeLocaleRepository;

    public ContactTypeLocaleServiceImpl(ContactTypeLocaleRepository contactTypeLocaleRepository) {
        this.contactTypeLocaleRepository = contactTypeLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ContactTypeEntity contactTypeEntity,
                                  LocaleEntity localeEntity,
                                  CreateContactTypeLocaleRequest request) {
        ContactTypeLocaleEntity entity = ContactTypeLocaleMapper.create(request, contactTypeEntity, localeEntity);
        contactTypeLocaleRepository.save(entity);
        log.info("ContactTypeLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ContactTypeLocaleEntity getEntityById(Long contactTypeId, Long id) {
        return contactTypeLocaleRepository
                .findByContactTypeEntity_IdAndIdAndIsActiveAndIsDeleted(contactTypeId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ContactTypeLocale not found with id: " + id));
    }

    @Transactional
    @Override
    public SuccessResponse update(ContactTypeLocaleEntity entity, UpdateContactTypeLocaleRequest request) {
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
}
