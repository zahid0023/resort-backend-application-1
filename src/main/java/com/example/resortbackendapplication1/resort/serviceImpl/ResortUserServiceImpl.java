package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.resort.dto.response.ResortUserResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortUserSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortUserMapper;
import com.example.resortbackendapplication1.resort.repository.ResortUserRepository;
import com.example.resortbackendapplication1.resort.service.ResortUserService;
import com.example.resortbackendapplication1.resortaccesstype.model.entity.ResortAccessTypeEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ResortUserServiceImpl implements ResortUserService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortUserSortField.allowedFields();

    private final ResortUserRepository resortUserRepository;

    public ResortUserServiceImpl(ResortUserRepository resortUserRepository) {
        this.resortUserRepository = resortUserRepository;
    }

    @Override
    public boolean isOwner(Long resortId, Long userId) {
        return resortUserRepository.existsByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(
                resortId, userId, true, false);
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortEntity resortEntity, UserEntity userEntity, ResortAccessTypeEntity resortAccessTypeEntity) {
        Optional<ResortUserEntity> existing = resortUserRepository
                .findByResortEntity_IdAndUserEntity_IdAndIsActiveAndIsDeleted(resortEntity.getId(), userEntity.getId(), true, false);

        if (existing.isPresent()) {
            throw new IllegalArgumentException("User is already a member of this resort");
        }

        ResortUserEntity entity = ResortUserMapper.create(resortEntity, userEntity, resortAccessTypeEntity);
        resortUserRepository.save(entity);
        log.info("ResortUser created with id: {}, resort id: {}, user id: {}",
                entity.getId(), resortEntity.getId(), userEntity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortUserEntity getEntityById(Long id, Long resortId) {
        return resortUserRepository.findByIdAndResortEntity_IdAndIsActiveAndIsDeleted(id, resortId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Resort user not found with id: " + id + ", resort id: " + resortId));
    }

    @Override
    public ResortUserResponse getById(Long id, Long resortId) {
        return new ResortUserResponse(ResortUserMapper.toDto(getEntityById(id, resortId)));
    }

    @Override
    public PaginatedResponse<ResortUserDto> getAll(Long resortId, PaginatedRequest request) {
        Page<@NonNull ResortUserEntity> page = resortUserRepository
                .findAllByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false,
                        request.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(ResortUserMapper::toDto));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortUserEntity entity, ResortAccessTypeEntity accessTypeEntity) {
        ResortUserMapper.update(entity, accessTypeEntity);
        resortUserRepository.save(entity);
        log.info("ResortUser updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortUserEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortUserRepository.save(entity);
        log.info("ResortUser soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
