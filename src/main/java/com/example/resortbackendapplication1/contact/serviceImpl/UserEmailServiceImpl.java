package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.dto.request.useremail.CreateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.request.useremail.UpdateUserEmailRequest;
import com.example.resortbackendapplication1.contact.dto.response.useremails.UserEmailResponse;
import com.example.resortbackendapplication1.contact.model.dto.UserEmailDto;
import com.example.resortbackendapplication1.contact.model.entity.UserEmailEntity;
import com.example.resortbackendapplication1.contact.model.mapper.UserEmailMapper;
import com.example.resortbackendapplication1.contact.repository.UserEmailRepository;
import com.example.resortbackendapplication1.contact.service.UserEmailService;
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
public class UserEmailServiceImpl implements UserEmailService {

    private final UserEmailRepository userEmailRepository;

    public UserEmailServiceImpl(UserEmailRepository userEmailRepository) {
        this.userEmailRepository = userEmailRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateUserEmailRequest request, UserEntity userEntity) {
        if (userEmailRepository.existsByEmailAndIsActiveAndIsDeleted(request.getEmail(), true, false)) {
            throw new IllegalStateException("Email '" + request.getEmail() + "' is already registered to another user");
        }

        UserEmailEntity entity = UserEmailMapper.create(request);
        userEntity.addUserEmailEntity(entity);

        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetCurrentPrimary(userEntity.getId());
        }

        userEmailRepository.save(entity);
        log.info("UserEmail created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public UserEmailEntity getEntityById(Long userId, Long id) {
        return userEmailRepository.findByUserEntity_IdAndIdAndIsActiveAndIsDeleted(userId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("UserEmail not found with id: " + id));
    }

    @Override
    public UserEmailResponse getById(Long userId, Long id) {
        UserEmailEntity entity = getEntityById(userId, id);
        UserEmailDto dto = UserEmailMapper.toDto(entity);
        return new UserEmailResponse(dto);
    }

    @Override
    public PaginatedResponse<UserEmailDto> getAll(Long userId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull UserEmailDto> dtoPage = userEmailRepository
                .findByUserEntity_IdAndIsActiveAndIsDeleted(userId, true, false, pageable)
                .map(UserEmailMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse update(UserEmailEntity entity, UpdateUserEmailRequest request) {
        boolean emailChanged = !entity.getEmail().equals(request.getEmail());
        if (emailChanged && userEmailRepository.existsByEmailAndIsActiveAndIsDeletedAndIdNot(
                request.getEmail(), true, false, entity.getId())) {
            throw new IllegalStateException("Email '" + request.getEmail() + "' is already registered to another user");
        }

        boolean becomingPrimary = Boolean.TRUE.equals(request.getIsPrimary()) && !Boolean.TRUE.equals(entity.getIsPrimary());
        if (becomingPrimary) {
            unsetCurrentPrimary(entity.getUserEntity().getId());
        }

        UserEmailMapper.update(entity, request);
        userEmailRepository.save(entity);
        log.info("UserEmail updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(UserEmailEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        userEmailRepository.save(entity);
        log.info("UserEmail soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void unsetCurrentPrimary(Long userId) {
        userEmailRepository.findByUserEntity_IdAndIsPrimaryAndIsActiveAndIsDeleted(userId, true, true, false)
                .ifPresent(current -> {
                    current.setIsPrimary(false);
                    userEmailRepository.save(current);
                });
    }
}
