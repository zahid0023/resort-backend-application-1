package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.CreateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.request.resortuserpermission.UpdateResortUserPermissionRequest;
import com.example.resortbackendapplication1.resort.dto.response.ResortUserPermissionResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortUserPermissionDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortUserPermissionSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortUserPermissionMapper;
import com.example.resortbackendapplication1.resort.repository.ResortUserPermissionRepository;
import com.example.resortbackendapplication1.resort.service.ResortUserPermissionService;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
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
public class ResortUserPermissionServiceImpl implements ResortUserPermissionService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortUserPermissionSortField.allowedFields();

    private final ResortUserPermissionRepository resortUserPermissionRepository;

    public ResortUserPermissionServiceImpl(ResortUserPermissionRepository resortUserPermissionRepository) {
        this.resortUserPermissionRepository = resortUserPermissionRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortUserPermissionRequest request,
                                  ResortUserEntity resortUserEntity,
                                  Map<Long, ResortPermissionTypeEntity> permissionTypeEntityMap) {
        List<ResortUserPermissionEntity> entities = ResortUserPermissionMapper.createAll(request, resortUserEntity, permissionTypeEntityMap);
        resortUserPermissionRepository.saveAll(entities);
        log.info("ResortUserPermissions created: {} for resort user id: {}", entities.size(), resortUserEntity.getId());
        return new SuccessResponse(true, resortUserEntity.getId());
    }

    @Override
    public ResortUserPermissionEntity getEntityById(Long id, Long resortUserId) {
        return resortUserPermissionRepository
                .findByIdAndResortUserEntity_IdAndIsActiveAndIsDeleted(id, resortUserId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Resort user permission not found with id: " + id +
                                ", resort user id: " + resortUserId));
    }

    @Override
    public ResortUserPermissionResponse getById(Long id, Long resortUserId) {
        return new ResortUserPermissionResponse(ResortUserPermissionMapper.toDto(getEntityById(id, resortUserId)));
    }

    @Override
    public PaginatedResponse<ResortUserPermissionDto> getAll(Long resortUserId, PaginatedRequest request) {
        Page<@NonNull ResortUserPermissionEntity> page = resortUserPermissionRepository
                .findAllByResortUserEntity_IdAndIsActiveAndIsDeleted(resortUserId, true, false,
                        request.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(ResortUserPermissionMapper::toDto));
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortUserPermissionEntity entity, UpdateResortUserPermissionRequest request) {
        ResortUserPermissionMapper.update(entity, request);
        resortUserPermissionRepository.save(entity);
        log.info("ResortUserPermission updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortUserPermissionEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortUserPermissionRepository.save(entity);
        log.info("ResortUserPermission soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
