package com.example.resortbackendapplication1.resort.serviceImpl;

import com.example.resortbackendapplication1.address.model.entity.CityEntity;
import com.example.resortbackendapplication1.address.model.entity.CountryEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.resort.dto.request.resort.CreateResortRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.ResortFilterRequest;
import com.example.resortbackendapplication1.resort.dto.request.resort.UpdateResortRequest;
import com.example.resortbackendapplication1.resort.dto.response.resorts.ResortResponse;
import com.example.resortbackendapplication1.resort.model.dto.ResortAddressDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortBasicInfoDto;
import com.example.resortbackendapplication1.resort.model.dto.ResortDto;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortAddressLocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortBasicInfoLocaleEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import com.example.resortbackendapplication1.resort.model.enums.ResortSearchField;
import com.example.resortbackendapplication1.resort.model.enums.ResortSortField;
import com.example.resortbackendapplication1.resort.model.mapper.ResortAddressLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortAddressMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortBasicInfoLocaleMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortBasicInfoMapper;
import com.example.resortbackendapplication1.resort.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.resort.repository.ResortRepository;
import com.example.resortbackendapplication1.resort.service.ResortAddressService;
import com.example.resortbackendapplication1.resort.service.ResortBasicInfoService;
import com.example.resortbackendapplication1.resort.service.ResortService;
import com.example.resortbackendapplication1.resort.specification.ResortSpecification;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import com.example.resortbackendapplication1.address.model.mapper.CityMapper;
import com.example.resortbackendapplication1.address.model.mapper.CountryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
public class ResortServiceImpl implements ResortService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortSearchField.allowedFields();

    private final ResortRepository resortRepository;
    private final ResortBasicInfoService resortBasicInfoService;
    private final ResortAddressService resortAddressService;

    public ResortServiceImpl(ResortRepository resortRepository,
                             ResortBasicInfoService resortBasicInfoService,
                             ResortAddressService resortAddressService) {
        this.resortRepository = resortRepository;
        this.resortBasicInfoService = resortBasicInfoService;
        this.resortAddressService = resortAddressService;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRequest request,
                                  UserEntity userEntity,
                                  ResortRoleTypeEntity resortRoleTypeEntity,
                                  ResortPermissionTypeEntity resortPermissionTypeEntity,
                                  CountryEntity countryEntity,
                                  CityEntity cityEntity,
                                  LocaleEntity localeEntity) {
        if (resortRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("Resort with code '" + request.getCode() + "' already exists");
        }

        ResortEntity entity = ResortMapper.create(request);

        // Every entity below is only attached to the object graph — none of them is persisted through
        // its own repository. ResortEntity's relations are all cascade=ALL, so the single
        // resortRepository.save(entity) call below inserts the resort and cascades to all of them.
        ResortUserEntity resortUserEntity = new ResortUserEntity();
        entity.addResortUserEntity(resortUserEntity);
        resortUserEntity.setUserEntity(userEntity);
        resortUserEntity.setResortRoleTypeEntity(resortRoleTypeEntity);
        resortUserEntity.setJoinedAt(Instant.now());

        ResortUserPermissionEntity resortUserPermissionEntity = new ResortUserPermissionEntity();
        resortUserEntity.addResortUserPermissionEntity(resortUserPermissionEntity);
        resortUserPermissionEntity.setResortPermissionTypeEntity(resortPermissionTypeEntity);
        resortUserPermissionEntity.setIsAllowed(true);

        ResortBasicInfoEntity resortBasicInfoEntity = ResortBasicInfoMapper.create(request.getBasicInfo());
        entity.assignResortBasicInfoEntity(resortBasicInfoEntity);
        ResortBasicInfoLocaleEntity resortBasicInfoLocaleEntity = ResortBasicInfoLocaleMapper.create(request.getBasicInfo().getLocale());
        resortBasicInfoEntity.addResortBasicInfoLocaleEntity(resortBasicInfoLocaleEntity);
        localeEntity.addResortBasicInfoLocaleEntity(resortBasicInfoLocaleEntity);

        ResortAddressEntity resortAddressEntity = ResortAddressMapper.create(request.getAddress());
        entity.assignResortAddressEntity(resortAddressEntity);
        resortAddressEntity.setCountryEntity(countryEntity);
        resortAddressEntity.setCityEntity(cityEntity);
        ResortAddressLocaleEntity resortAddressLocaleEntity = ResortAddressLocaleMapper.create(request.getAddress().getLocale());
        resortAddressEntity.addResortAddressLocaleEntity(resortAddressLocaleEntity);
        localeEntity.addResortAddressLocaleEntity(resortAddressLocaleEntity);

        resortRepository.save(entity);

        log.info("Resort created with id: {}, owner user id: {}", entity.getId(), userEntity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortEntity getEntityById(Long id) {
        return resortRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort not found with id: " + id));
    }

    @Override
    public ResortResponse getById(Long id) {
        ResortEntity entity = getEntityById(id);

        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(id);
        ResortBasicInfoDto resortBasicInfoDto = ResortBasicInfoMapper.toDto(resortBasicInfoEntity).build();

        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(id);
        ResortAddressDto resortAddressDto = ResortAddressMapper.toDto(resortAddressEntity)
                .country(CountryMapper.toDto(resortAddressEntity.getCountryEntity()).build())
                .city(CityMapper.toDto(resortAddressEntity.getCityEntity()).build())
                .build();

        ResortDto dto = ResortMapper.toDto(entity)
                .basicInfo(resortBasicInfoDto)
                .address(resortAddressDto)
                .build();
        return new ResortResponse(dto);
    }

    @Override
    public PaginatedResponse<ResortDto> getAll(ResortFilterRequest request) {
        Specification<@NonNull ResortEntity> specification =
                ResortSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortSortField.localeSortFields());
        Page<@NonNull ResortDto> page = resortRepository
                .findAll(specification, pageable)
                .map(entity -> ResortMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Override
    public PaginatedResponse<ResortDto> getMyResorts(Long authenticatedUserId, PaginatedRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ResortDto> page = resortRepository
                .findAllByMemberUserId(authenticatedUserId, pageable)
                .map(entity -> ResortMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, Set.of());
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortEntity entity, UpdateResortRequest request) {
        ResortMapper.update(entity, request);
        resortRepository.save(entity);
        log.info("Resort updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        // ResortBasicInfo/ResortAddress are soft-deleted in place, not via their own service — both are
        // cascade=ALL and already managed entities in this transaction, so the field changes flush with
        // entity below.
        ResortBasicInfoEntity resortBasicInfoEntity = resortBasicInfoService.getEntityByResortId(entity.getId());
        resortBasicInfoEntity.setIsDeleted(true);
        resortBasicInfoEntity.setIsActive(false);

        ResortAddressEntity resortAddressEntity = resortAddressService.getEntityByResortId(entity.getId());
        resortAddressEntity.setIsDeleted(true);
        resortAddressEntity.setIsActive(false);

        resortRepository.save(entity);

        log.info("Resort soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
