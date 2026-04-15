package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.auth.model.enitty.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.resorts.CreateResortRequest;
import com.example.resortbackendapplication1.dto.request.resorts.UpdateResortRequest;
import com.example.resortbackendapplication1.dto.response.resorts.ResortResponse;
import com.example.resortbackendapplication1.model.dto.ResortDto;
import com.example.resortbackendapplication1.model.entity.*;
import com.example.resortbackendapplication1.model.mapper.ResortMapper;
import com.example.resortbackendapplication1.model.projection.ResortSummary;
import com.example.resortbackendapplication1.repository.ResortRepository;
import com.example.resortbackendapplication1.repository.UserResortAccessRepository;
import com.example.resortbackendapplication1.service.CityService;
import com.example.resortbackendapplication1.service.CountryService;
import com.example.resortbackendapplication1.service.ResortService;
import com.example.resortbackendapplication1.utils.Pagination;
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
    private final CountryService countryService;
    private final CityService cityService;

    public ResortServiceImpl(
            ResortRepository resortRepository,
            CountryService countryService,
            CityService cityService
    ) {
        this.resortRepository = resortRepository;
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @Override
    @Transactional
    public SuccessResponse createResort(UserEntity userEntity,
                                        ResortAccessTypeEntity resortAccessTypeEntity,
                                        CountryEntity countryEntity,
                                        CityEntity cityEntity,
                                        CreateResortRequest request) {

        ResortEntity resortEntity = resortRepository.save(ResortMapper.fromRequest(request, userEntity, resortAccessTypeEntity, countryEntity, cityEntity));
        return new SuccessResponse(true, resortEntity.getId());
    }

    @Override
    public ResortResponse getResort(Long id) {
        ResortEntity entity = getResortById(id);
        return new ResortResponse(ResortMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortDto> getAllResorts(UserEntity userEntity, Pageable pageable) {
        Page<@NonNull ResortSummary> page;

        if (userEntity.getRoleEntity().getName().equals("ADMIN")) {
            page = resortRepository.findAllByIsActiveAndIsDeleted(true, false, pageable, ResortSummary.class);
        } else {
            page = resortRepository.findAllByIsActiveAndIsDeletedAndUserResortAccessesEntities_UserEntity(
                    true, false, userEntity, pageable, ResortSummary.class
            );
        }

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
