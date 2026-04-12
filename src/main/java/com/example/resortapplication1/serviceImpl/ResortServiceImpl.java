package com.example.resortapplication1.serviceImpl;

import com.example.resortapplication1.auth.model.enitty.UserEntity;
import com.example.resortapplication1.auth.service.UserService;
import com.example.resortapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortapplication1.commons.dto.response.SuccessResponse;
import com.example.resortapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortapplication1.dto.response.resorts.ResortResponse;
import com.example.resortapplication1.model.dto.ResortDto;
import com.example.resortapplication1.model.entity.*;
import com.example.resortapplication1.model.mapper.ResortMapper;
import com.example.resortapplication1.model.projection.ResortSummary;
import com.example.resortapplication1.repository.ResortRepository;
import com.example.resortapplication1.repository.UserResortAccessRepository;
import com.example.resortapplication1.service.CityService;
import com.example.resortapplication1.service.CountryService;
import com.example.resortapplication1.service.ResortAccessTypeService;
import com.example.resortapplication1.service.ResortService;
import com.example.resortapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResortServiceImpl implements ResortService {

    private final ResortRepository resortRepository;
    private final UserResortAccessRepository userResortAccessRepository;
    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;
    private final ResortAccessTypeService resortAccessTypeService;

    public ResortServiceImpl(
            ResortRepository resortRepository,
            UserResortAccessRepository userResortAccessRepository,
            UserService userService,
            CountryService countryService,
            CityService cityService,
            ResortAccessTypeService resortAccessTypeService
    ) {
        this.resortRepository = resortRepository;
        this.userResortAccessRepository = userResortAccessRepository;
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.resortAccessTypeService = resortAccessTypeService;
    }

    @Override
    @Transactional
    public SuccessResponse createResort(CreateResortRequest request) {
        UserEntity user = userService.getAuthenticatedUserEntity();
        CountryEntity country = countryService.getCountryEntity(request.getCountryId());
        CityEntity city = request.getCityId() != null
                ? cityService.getCityEntity(request.getCityId())
                : null;

        ResortEntity resort = ResortMapper.fromRequest(request, country, city);
        resort = resortRepository.save(resort);

        ResortAccessTypeEntity ownerAccessType = resortAccessTypeService.getResortAccessTypeByCode("OWNER");

        UserResortAccessEntity access = new UserResortAccessEntity();
        access.setUserEntity(user);
        access.setResortEntity(resort);
        access.setAccessTypeEntity(ownerAccessType);
        userResortAccessRepository.save(access);

        return new SuccessResponse(true, resort.getId());
    }

    @Override
    public ResortResponse getResort(Long id) {
        ResortEntity entity = getResortById(id);
        return new ResortResponse(ResortMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortDto> getAllResorts(Pageable pageable) {
        Page<@NonNull ResortSummary> page = resortRepository
                .findAllByIsActiveAndIsDeleted(true, false, pageable, ResortSummary.class);

        Page<@NonNull ResortDto> dtoPage = page.map(p ->
                ResortDto.builder()
                        .id(p.getId())
                        .uuid(p.getUuid())
                        .name(p.getName())
                        .description(p.getDescription())
                        .address(p.getAddress())
                        .countryId(p.getCountryId())
                        .cityId(p.getCityId())
                        .contactEmail(p.getContactEmail())
                        .contactPhone(p.getContactPhone())
                        .build()
        );
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public ResortResponse updateResort(Long id, UpdateResortRequest request) {
        ResortEntity entity = getResortById(id);
        CountryEntity country = request.getCountryId() != null
                ? countryService.getCountryEntity(request.getCountryId())
                : null;
        CityEntity city = request.getCityId() != null
                ? cityService.getCityEntity(request.getCityId())
                : null;
        ResortMapper.updateEntity(entity, request, country, city);
        entity = resortRepository.save(entity);
        return new ResortResponse(ResortMapper.toDto(entity));
    }

    @Override
    public SuccessResponse deleteResort(Long id) {
        ResortEntity entity = getResortById(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRepository.save(entity);
        return new SuccessResponse(true, id);
    }

    @Override
    public ResortEntity getResortById(Long id) {
        return resortRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Resort with id: " + id + " was not found."));
    }
}
