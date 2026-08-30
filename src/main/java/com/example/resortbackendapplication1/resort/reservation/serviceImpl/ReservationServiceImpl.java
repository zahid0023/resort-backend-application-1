package com.example.resortbackendapplication1.resort.reservation.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationSourceMapper;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusMapper;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.CreateReservationRequest;
import com.example.resortbackendapplication1.resort.reservation.dto.request.reservation.ReservationFilterRequest;
import com.example.resortbackendapplication1.resort.reservation.dto.response.reservations.ReservationResponse;
import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import com.example.resortbackendapplication1.resort.reservation.model.enums.ReservationSortField;
import com.example.resortbackendapplication1.resort.reservation.model.mapper.ReservationMapper;
import com.example.resortbackendapplication1.resort.reservation.repository.ReservationRepository;
import com.example.resortbackendapplication1.resort.reservation.service.ReservationService;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ReservationSortField.allowedFields();

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateReservationRequest request,
                                  UserEntity userEntity,
                                  ResortRoomEntity resortRoomEntity,
                                  ReservationStatusEntity reservationStatusEntity,
                                  ReservationSourceEntity reservationSourceEntity,
                                  CurrencyEntity currencyEntity,
                                  PriceUnitEntity priceUnitEntity,
                                  BigDecimal totalPrice,
                                  BookingGroupEntity bookingGroupEntity) {
        ReservationEntity entity = ReservationMapper.create(
                request, reservationStatusEntity, reservationSourceEntity, currencyEntity, priceUnitEntity, totalPrice);
        userEntity.addReservationEntity(entity);
        resortRoomEntity.addReservationEntity(entity);
        bookingGroupEntity.addReservationEntity(entity);

        reservationRepository.save(entity);
        log.info("Reservation created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ReservationEntity getEntityById(Long resortRoomId, Long id) {
        return reservationRepository.findByIdAndResortRoomEntity_IdAndIsActiveAndIsDeleted(id, resortRoomId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));
    }

    @Override
    public ReservationResponse getById(Long resortRoomId, Long id) {
        ReservationEntity entity = getEntityById(resortRoomId, id);
        return new ReservationResponse(toFullDto(entity));
    }

    @Override
    public PaginatedResponse<ReservationDto> getAll(Long resortRoomId, ReservationFilterRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ReservationDto> page = reservationRepository
                .findByResortRoomEntity_IdAndIsActiveAndIsDeleted(resortRoomId, true, false, pageable)
                .map(this::toFullDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, Set.of());
    }

    @Override
    public PaginatedResponse<ReservationDto> getAllForResort(Long resortId, ReservationFilterRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ReservationDto> page = reservationRepository
                .findByResortRoomEntity_ResortRoomCategoryEntity_ResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable)
                .map(this::toFullDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, Set.of());
    }

    @Transactional
    @Override
    public SuccessResponse transitionStatus(ReservationEntity entity, ReservationStatusEntity newReservationStatusEntity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        reservationRepository.save(entity);

        ReservationEntity newEntity = ReservationMapper.transition(entity, newReservationStatusEntity);
        entity.getUserEntity().addReservationEntity(newEntity);
        entity.getResortRoomEntity().addReservationEntity(newEntity);
        entity.getBookingGroupEntity().addReservationEntity(newEntity);
        reservationRepository.save(newEntity);

        log.info("Reservation {} superseded by {} (status transition)", entity.getId(), newEntity.getId());
        return new SuccessResponse(true, newEntity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ReservationEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        reservationRepository.save(entity);
        log.info("Reservation soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private ReservationDto toFullDto(ReservationEntity entity) {
        return ReservationMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .reservationStatus(ReservationStatusMapper.toDto(entity.getReservationStatusEntity()).build())
                .reservationSource(ReservationSourceMapper.toDto(entity.getReservationSourceEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .build();
    }
}
