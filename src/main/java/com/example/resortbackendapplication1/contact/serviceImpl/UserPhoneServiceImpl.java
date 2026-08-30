package com.example.resortbackendapplication1.contact.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.contact.dto.request.userphone.CreateUserPhoneRequest;
import com.example.resortbackendapplication1.contact.dto.request.userphone.UpdateUserPhoneRequest;
import com.example.resortbackendapplication1.contact.dto.response.userphones.UserPhoneResponse;
import com.example.resortbackendapplication1.contact.model.dto.UserPhoneDto;
import com.example.resortbackendapplication1.contact.model.entity.UserPhoneEntity;
import com.example.resortbackendapplication1.contact.model.mapper.UserPhoneMapper;
import com.example.resortbackendapplication1.contact.repository.UserPhoneRepository;
import com.example.resortbackendapplication1.contact.service.UserPhoneService;
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
public class UserPhoneServiceImpl implements UserPhoneService {

    private final UserPhoneRepository userPhoneRepository;

    public UserPhoneServiceImpl(UserPhoneRepository userPhoneRepository) {
        this.userPhoneRepository = userPhoneRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateUserPhoneRequest request, UserEntity userEntity) {
        if (userPhoneRepository.existsByPhoneAndIsActiveAndIsDeleted(request.getPhone(), true, false)) {
            throw new IllegalStateException("Phone '" + request.getPhone() + "' is already registered to another user");
        }

        UserPhoneEntity entity = UserPhoneMapper.create(request);
        userEntity.addUserPhoneEntity(entity);

        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            unsetCurrentPrimary(userEntity.getId());
        }

        userPhoneRepository.save(entity);
        log.info("UserPhone created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public UserPhoneEntity getEntityById(Long userId, Long id) {
        return userPhoneRepository.findByUserEntity_IdAndIdAndIsActiveAndIsDeleted(userId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("UserPhone not found with id: " + id));
    }

    @Override
    public UserPhoneResponse getById(Long userId, Long id) {
        UserPhoneEntity entity = getEntityById(userId, id);
        UserPhoneDto dto = UserPhoneMapper.toDto(entity);
        return new UserPhoneResponse(dto);
    }

    @Override
    public PaginatedResponse<UserPhoneDto> getAll(Long userId, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull UserPhoneDto> dtoPage = userPhoneRepository
                .findByUserEntity_IdAndIsActiveAndIsDeleted(userId, true, false, pageable)
                .map(UserPhoneMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Transactional
    @Override
    public SuccessResponse update(UserPhoneEntity entity, UpdateUserPhoneRequest request) {
        boolean phoneChanged = !entity.getPhone().equals(request.getPhone());
        if (phoneChanged && userPhoneRepository.existsByPhoneAndIsActiveAndIsDeletedAndIdNot(
                request.getPhone(), true, false, entity.getId())) {
            throw new IllegalStateException("Phone '" + request.getPhone() + "' is already registered to another user");
        }

        boolean becomingPrimary = Boolean.TRUE.equals(request.getIsPrimary()) && !Boolean.TRUE.equals(entity.getIsPrimary());
        if (becomingPrimary) {
            unsetCurrentPrimary(entity.getUserEntity().getId());
        }

        UserPhoneMapper.update(entity, request);
        userPhoneRepository.save(entity);
        log.info("UserPhone updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(UserPhoneEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        userPhoneRepository.save(entity);
        log.info("UserPhone soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void unsetCurrentPrimary(Long userId) {
        userPhoneRepository.findByUserEntity_IdAndIsPrimaryAndIsActiveAndIsDeleted(userId, true, true, false)
                .ifPresent(current -> {
                    current.setIsPrimary(false);
                    userPhoneRepository.save(current);
                });
    }
}
