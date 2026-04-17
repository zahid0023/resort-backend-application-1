package com.example.resortbackendapplication1.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.pricetypes.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.request.pricetypes.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.model.dto.PriceTypeDto;
import com.example.resortbackendapplication1.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.model.mapper.PriceTypeMapper;
import com.example.resortbackendapplication1.repository.PriceTypeRepository;
import com.example.resortbackendapplication1.service.PriceTypeService;
import com.example.resortbackendapplication1.utils.Pagination;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceTypeServiceImpl implements PriceTypeService {

    private final PriceTypeRepository priceTypeRepository;

    public PriceTypeServiceImpl(PriceTypeRepository priceTypeRepository) {
        this.priceTypeRepository = priceTypeRepository;
    }

    @Override
    public SuccessResponse createPriceType(CreatePriceTypeRequest request) {
        PriceTypeEntity entity = PriceTypeMapper.fromRequest(request);
        entity = priceTypeRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PriceTypeEntity getPriceTypeEntity(Long id) {
        return priceTypeRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Price Type with id: " + id + " was not found."));
    }

    @Override
    public PriceTypeResponse getPriceType(Long id) {
        PriceTypeEntity entity = getPriceTypeEntity(id);
        PriceTypeDto dto = PriceTypeMapper.toDto(entity);
        return new PriceTypeResponse(dto);
    }

    @Override
    public PaginatedResponse<PriceTypeDto> getAllPriceTypes(Pageable pageable) {
        Page<@NonNull PriceTypeEntity> page = priceTypeRepository.findAllByIsActiveAndIsDeleted(true, false, pageable);
        Page<@NonNull PriceTypeDto> dtoPage = page.map(PriceTypeMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public SuccessResponse updatePriceType(Long id, UpdatePriceTypeRequest request) {
        PriceTypeEntity entity = getPriceTypeEntity(id);
        PriceTypeMapper.updateEntity(entity, request);
        entity = priceTypeRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SuccessResponse deletePriceType(Long id) {
        PriceTypeEntity entity = getPriceTypeEntity(id);
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        priceTypeRepository.save(entity);
        return new SuccessResponse(true, id);
    }
}
