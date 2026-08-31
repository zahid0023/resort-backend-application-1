package com.example.resortbackendapplication1.resort.room.serviceImpl;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.bedtype.model.mapper.BedTypeMapper;
import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.CreateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.ResortRoomFilterRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroom.UpdateResortRoomRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroombed.CreateResortRoomBedRequest;
import com.example.resortbackendapplication1.resort.room.dto.request.resortroomprice.CreateResortRoomMainPriceRequest;
import com.example.resortbackendapplication1.resort.room.dto.response.resortrooms.ResortRoomResponse;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomBedDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomDto;
import com.example.resortbackendapplication1.resort.room.model.dto.ResortRoomMetaDto;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomBedEntity;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryBedDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryMetaDto;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomLocaleEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMetaEntity;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomSearchField;
import com.example.resortbackendapplication1.resort.room.model.enums.ResortRoomSortField;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomBedMapper;
import com.example.resortbackendapplication1.resort.roomcategory.model.mapper.ResortRoomCategoryMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomLocaleMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMetaMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomPriceMapper;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomRepository;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomMetaService;
import com.example.resortbackendapplication1.resort.room.service.ResortRoomService;
import com.example.resortbackendapplication1.resort.room.specification.ResortRoomSpecification;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import com.example.resortbackendapplication1.roomstatus.model.mapper.RoomStatusMapper;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResortRoomServiceImpl implements ResortRoomService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortRoomSortField.allowedFields();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = ResortRoomSearchField.allowedFields();

    private final ResortRoomRepository resortRoomRepository;
    private final ResortRoomMetaService resortRoomMetaService;

    public ResortRoomServiceImpl(ResortRoomRepository resortRoomRepository,
                                 ResortRoomMetaService resortRoomMetaService) {
        this.resortRoomRepository = resortRoomRepository;
        this.resortRoomMetaService = resortRoomMetaService;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateResortRoomRequest request,
                                  ResortRoomCategoryEntity resortRoomCategoryEntity,
                                  RoomStatusEntity roomStatusEntity,
                                  LocaleEntity localeEntity,
                                  UnitEntity roomSizeUnitEntity,
                                  List<BedTypeEntity> bedTypeEntities,
                                  List<CurrencyEntity> currencyEntities,
                                  List<PriceUnitEntity> priceUnitEntities) {
        if (resortRoomRepository.existsByResortRoomCategoryEntity_ResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
                resortRoomCategoryEntity.getResortEntity().getId(), request.getCode(), true, false)) {
            throw new IllegalStateException("Resort already has a room with code: " + request.getCode());
        }

        List<CreateResortRoomBedRequest> bedRequests = request.getBeds() != null ? request.getBeds() : List.of();
        Set<Long> requestedBedTypeIds = bedRequests.stream()
                .map(CreateResortRoomBedRequest::getBedTypeId)
                .collect(Collectors.toSet());
        if (requestedBedTypeIds.size() != bedRequests.size()) {
            throw new IllegalStateException("Duplicate bed type ids are not allowed in the same request");
        }
        Map<Long, BedTypeEntity> bedTypesById = bedTypeEntities.stream()
                .collect(Collectors.toMap(BedTypeEntity::getId, bedTypeEntity -> bedTypeEntity));

        List<CreateResortRoomMainPriceRequest> priceGroupRequests = request.getPrices() != null ? request.getPrices() : List.of();
        Set<Long> requestedCurrencyIds = priceGroupRequests.stream()
                .map(CreateResortRoomMainPriceRequest::getCurrencyId)
                .collect(Collectors.toSet());
        if (requestedCurrencyIds.size() != priceGroupRequests.size()) {
            throw new IllegalStateException("Duplicate currency ids are not allowed in the same request");
        }
        Map<Long, CurrencyEntity> currenciesById = currencyEntities.stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, currencyEntity -> currencyEntity));
        Map<Long, PriceUnitEntity> priceUnitsById = priceUnitEntities.stream()
                .collect(Collectors.toMap(PriceUnitEntity::getId, priceUnitEntity -> priceUnitEntity));

        ResortRoomEntity entity = ResortRoomMapper.create(request);
        resortRoomCategoryEntity.addResortRoomEntity(entity);
        roomStatusEntity.addResortRoomEntity(entity);

        ResortRoomLocaleEntity resortRoomLocaleEntity = ResortRoomLocaleMapper.create(request.getLocale());
        entity.addResortRoomLocaleEntity(resortRoomLocaleEntity);
        localeEntity.addResortRoomLocaleEntity(resortRoomLocaleEntity);

        if (request.getMeta() != null) {
            ResortRoomMetaEntity resortRoomMetaEntity = ResortRoomMetaMapper.create(request.getMeta(), roomSizeUnitEntity);
            entity.assignResortRoomMetaEntity(resortRoomMetaEntity);
        }

        for (CreateResortRoomBedRequest bedRequest : bedRequests) {
            BedTypeEntity bedTypeEntity = bedTypesById.get(bedRequest.getBedTypeId());
            ResortRoomBedEntity resortRoomBedEntity = ResortRoomBedMapper.create(bedRequest);
            entity.addResortRoomBedEntity(resortRoomBedEntity);
            bedTypeEntity.addResortRoomBedEntity(resortRoomBedEntity);
        }

        for (CreateResortRoomMainPriceRequest priceGroup : priceGroupRequests) {
            attachPriceGroup(entity, priceGroup, currenciesById, priceUnitsById);
        }

        resortRoomRepository.save(entity);
        log.info("ResortRoom created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    /**
     * Delegates entity-building to {@link ResortRoomPriceMapper#createMain} — the same shared mapper method
     * {@code ResortRoomPriceServiceImpl} uses for its standalone {@code POST /prices/main} endpoint — so the
     * weekday/weekend-cannot-exceed-base rule and entity shape can't drift between the two call sites. Kept
     * in the mapper layer rather than called via {@code ResortRoomPriceService} since a ServiceImpl must
     * never call another entity's Service. Mirrors {@code ResortRoomCategoryServiceImpl.attachPriceGroup}.
     */
    private void attachPriceGroup(ResortRoomEntity entity,
                                  CreateResortRoomMainPriceRequest priceGroup,
                                  Map<Long, CurrencyEntity> currenciesById,
                                  Map<Long, PriceUnitEntity> priceUnitsById) {
        CurrencyEntity currencyEntity = currenciesById.get(priceGroup.getCurrencyId());
        PriceUnitEntity priceUnitEntity = priceUnitsById.get(priceGroup.getPriceUnitId());

        ResortRoomPriceMapper.createMain(priceGroup, entity, priceUnitEntity, currencyEntity);
    }

    @Override
    public ResortRoomEntity getEntityById(Long resortRoomCategoryId, Long id) {
        return resortRoomRepository.findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(resortRoomCategoryId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoom not found with id: " + id));
    }

    @Override
    public ResortRoomEntity getEntityByResortId(Long resortId, Long id) {
        return resortRoomRepository.findByResortRoomCategoryEntity_ResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ResortRoom not found with id: " + id));
    }

    @Override
    public ResortRoomResponse getById(Long resortRoomCategoryId, Long id,
                                      ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                                      List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback) {
        ResortRoomEntity entity = getEntityById(resortRoomCategoryId, id);
        return new ResortRoomResponse(buildDto(entity, resortRoomCategoryMetaFallback, resortRoomCategoryBedsFallback));
    }

    @Override
    public ResortRoomDto buildDto(ResortRoomEntity entity,
                                  ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                                  List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback) {
        ResortRoomMetaDto metaDto = resortRoomMetaService.findEntityByResortRoomId(entity.getId())
                .map(resortRoomMetaEntity -> ResortRoomMetaMapper.toDto(resortRoomMetaEntity).build())
                .orElseGet(() -> ResortRoomMetaMapper.fromCategory(resortRoomCategoryMetaFallback));
        return ResortRoomMapper.toDto(entity)
                .resortRoomCategory(ResortRoomCategoryMapper.toDto(entity.getResortRoomCategoryEntity()).build())
                .roomStatus(RoomStatusMapper.toDto(entity.getRoomStatusEntity()).build())
                .meta(metaDto)
                .beds(mapBeds(entity, resortRoomCategoryBedsFallback))
                .build();
    }

    private List<ResortRoomBedDto> mapBeds(ResortRoomEntity entity, List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback) {
        List<ResortRoomBedDto> ownBeds = entity.getResortRoomBedEntities().stream()
                .filter(bedEntity -> Boolean.TRUE.equals(bedEntity.getIsActive())
                        && Boolean.FALSE.equals(bedEntity.getIsDeleted()))
                .map(bedEntity -> ResortRoomBedMapper.toDto(bedEntity)
                        .bedType(BedTypeMapper.toDto(bedEntity.getBedTypeEntity()).build())
                        .build())
                .toList();
        if (!ownBeds.isEmpty()) {
            return ownBeds;
        }
        return resortRoomCategoryBedsFallback.stream().map(ResortRoomBedMapper::fromCategory).toList();
    }

    @Override
    public PaginatedResponse<ResortRoomDto> getAll(Long resortRoomCategoryId, ResortRoomFilterRequest request,
                                                    ResortRoomCategoryMetaDto resortRoomCategoryMetaFallback,
                                                    List<ResortRoomCategoryBedDto> resortRoomCategoryBedsFallback) {
        Specification<@NonNull ResortRoomEntity> specification =
                ResortRoomSpecification.filter(resortRoomCategoryId, request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS, ResortRoomSortField.localeSortFields());
        Page<@NonNull ResortRoomDto> page = resortRoomRepository
                .findAll(specification, pageable)
                .map(entity -> buildDto(entity, resortRoomCategoryMetaFallback, resortRoomCategoryBedsFallback));
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ResortRoomEntity entity, UpdateResortRoomRequest request) {
        ResortRoomMapper.update(entity, request);
        resortRoomRepository.save(entity);
        log.info("ResortRoom updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse updateStatus(ResortRoomEntity entity, RoomStatusEntity roomStatusEntity) {
        if (!entity.getRoomStatusEntity().getId().equals(roomStatusEntity.getId())) {
            entity.getRoomStatusEntity().removeResortRoomEntity(entity);
            roomStatusEntity.addResortRoomEntity(entity);
            resortRoomRepository.save(entity);
        }
        log.info("ResortRoom status updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ResortRoomEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getResortRoomLocaleEntities().forEach(localeEntity -> {
            localeEntity.setIsDeleted(true);
            localeEntity.setIsActive(false);
        });

        entity.getResortRoomBedEntities().forEach(bedEntity -> {
            bedEntity.setIsDeleted(true);
            bedEntity.setIsActive(false);
        });

        // ResortRoomMeta is soft-deleted in place, not via its own service — it's cascade=ALL
        // and already a managed entity in this transaction, so the field changes flush with entity below.
        // A room may have no own meta row at all (inheriting from its category), hence the null-safe find.
        resortRoomMetaService.findEntityByResortRoomId(entity.getId()).ifPresent(resortRoomMetaEntity -> {
            resortRoomMetaEntity.setIsDeleted(true);
            resortRoomMetaEntity.setIsActive(false);
        });

        resortRoomRepository.save(entity);
        log.info("ResortRoom soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
