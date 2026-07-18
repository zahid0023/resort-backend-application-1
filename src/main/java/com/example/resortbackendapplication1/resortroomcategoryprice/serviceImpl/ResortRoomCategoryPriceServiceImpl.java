package com.example.resortbackendapplication1.resortroomcategoryprice.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.CreateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.ResortRoomCategoryPriceFilterRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.request.UpdateResortRoomCategoryPriceRequest;
import com.example.resortbackendapplication1.resortroomcategoryprice.dto.response.ResortRoomCategoryPriceResponse;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.dto.ResortRoomCategoryPriceDto;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceEntity;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.enums.ResortRoomCategoryPriceSortField;
import com.example.resortbackendapplication1.resortroomcategoryprice.model.mapper.ResortRoomCategoryPriceMapper;
import com.example.resortbackendapplication1.resortroomcategoryprice.repository.ResortRoomCategoryPriceRepository;
import com.example.resortbackendapplication1.resortroomcategoryprice.service.ResortRoomCategoryPriceService;
import com.example.resortbackendapplication1.resortroomcategoryprice.specification.ResortRoomCategoryPriceSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ResortRoomCategoryPriceServiceImpl implements ResortRoomCategoryPriceService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomCategoryPriceSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository;

    public ResortRoomCategoryPriceServiceImpl(ResortRoomCategoryPriceRepository resortRoomCategoryPriceRepository) {
        this.resortRoomCategoryPriceRepository = resortRoomCategoryPriceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomCategoryPriceRequest request,
                                   ResortRoomCategoryEntity resortRoomCategoryEntity,
                                   PriceTypeEntity priceTypeEntity,
                                   PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryPriceEntity entity = ResortRoomCategoryPriceMapper.create(
                request, resortRoomCategoryEntity, priceTypeEntity, priceUnitEntity);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ResortRoomCategoryPriceEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomCategoryPriceRepository
                .findByIdAndResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(id, resortRoomCategoryId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoomCategoryPrice not found with id: " + id));
    }

    @Override
    public ResortRoomCategoryPriceResponse getById(Long resortRoomCategoryId, Long id) {
        ResortRoomCategoryPriceEntity entity = getEntityById(resortRoomCategoryId, id);
        return new ResortRoomCategoryPriceResponse(ResortRoomCategoryPriceMapper.toDto(entity));
    }

    @Override
    public PaginatedResponse<ResortRoomCategoryPriceDto> getAll(ResortRoomCategoryPriceFilterRequest request,
                                                                 Long resortRoomCategoryId) {
        Page<@NonNull ResortRoomCategoryPriceDto> page = resortRoomCategoryPriceRepository
                .findAll(ResortRoomCategoryPriceSpecification.filter(request, resortRoomCategoryId),
                        request.toPageable(ALLOWED_SORT_FIELDS))
                .map(ResortRoomCategoryPriceMapper::toDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomCategoryPriceEntity entity,
                                   UpdateResortRoomCategoryPriceRequest request,
                                   PriceTypeEntity priceTypeEntity,
                                   PriceUnitEntity priceUnitEntity) {
        ResortRoomCategoryPriceMapper.update(entity, request, priceTypeEntity, priceUnitEntity);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomCategoryPriceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        resortRoomCategoryPriceRepository.save(entity);
        log.info("ResortRoomCategoryPrice soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
