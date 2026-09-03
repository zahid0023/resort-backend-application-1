package com.example.resortbackendapplication1.mail.provider.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.CreateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.dto.request.mailprovider.configfield.UpdateMailProviderConfigFieldRequest;
import com.example.resortbackendapplication1.mail.provider.model.dto.MailProviderConfigFieldDto;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigFieldEntity;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderEntity;
import com.example.resortbackendapplication1.mail.provider.model.mapper.MailProviderConfigFieldMapper;
import com.example.resortbackendapplication1.mail.provider.repository.MailProviderConfigFieldRepository;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigFieldService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MailProviderConfigFieldServiceImpl implements MailProviderConfigFieldService {

    private final MailProviderConfigFieldRepository mailProviderConfigFieldRepository;

    public MailProviderConfigFieldServiceImpl(
            MailProviderConfigFieldRepository mailProviderConfigFieldRepository) {
        this.mailProviderConfigFieldRepository = mailProviderConfigFieldRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateMailProviderConfigFieldRequest request, MailProviderEntity providerEntity) {
        if (mailProviderConfigFieldRepository.existsByMailProviderEntity_IdAndKeyAndIsActiveAndIsDeleted(
                providerEntity.getId(), request.getKey(), true, false)) {
            throw new IllegalStateException("Config field with key '" + request.getKey() + "' already exists for this provider");
        }

        MailProviderConfigFieldEntity entity = MailProviderConfigFieldMapper.create(request);
        providerEntity.addMailProviderConfigFieldEntity(entity);
        mailProviderConfigFieldRepository.save(entity);
        log.info("MailProviderConfigField created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public MailProviderConfigFieldEntity getEntityById(Long mailProviderId, Long id) {
        return mailProviderConfigFieldRepository
                .findByMailProviderEntity_IdAndIdAndIsActiveAndIsDeleted(mailProviderId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("MailProviderConfigField not found with id: " + id));
    }

    @Override
    public List<MailProviderConfigFieldDto> getAll(Long mailProviderId) {
        return mailProviderConfigFieldRepository
                .findByMailProviderEntity_IdAndIsActiveAndIsDeleted(mailProviderId, true, false)
                .stream()
                .map(entity -> MailProviderConfigFieldMapper.toDto(entity).build())
                .toList();
    }

    @Transactional
    @Override
    public SuccessResponse update(MailProviderConfigFieldEntity entity, UpdateMailProviderConfigFieldRequest request) {
        MailProviderConfigFieldMapper.update(entity, request);
        mailProviderConfigFieldRepository.save(entity);
        log.info("MailProviderConfigField updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(MailProviderConfigFieldEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        mailProviderConfigFieldRepository.save(entity);
        log.info("MailProviderConfigField soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
