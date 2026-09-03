package com.example.resortbackendapplication1.resort.booking.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.mapper.BookingSourceMapper;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.currency.model.dto.CurrencyDto;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.CreateResortBookingRequest;
import com.example.resortbackendapplication1.resort.booking.dto.request.booking.ResortBookingFilterRequest;
import com.example.resortbackendapplication1.resort.booking.model.dto.ResortBookingDto;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.booking.model.enums.ResortBookingSortField;
import com.example.resortbackendapplication1.resort.booking.model.mapper.ResortBookingMapper;
import com.example.resortbackendapplication1.resort.booking.repository.ResortBookingRepository;
import com.example.resortbackendapplication1.resort.booking.service.ResortBookingService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.roomreservation.dto.request.roomreservation.CreateResortRoomReservationRequest;
import com.example.resortbackendapplication1.resort.roomreservation.model.dto.ResortRoomReservationDto;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import com.example.resortbackendapplication1.resort.roomreservation.service.ResortRoomReservationService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Injects {@code ResortRoomReservationService} directly — a deliberate, named exception to the usual
 * controller-orchestrates-cross-domain rule, since a reservation's whole purpose is to serve availability and
 * booking (see {@code ResortBookingService#createPosBooking}'s javadoc).
 */
@Service
@Slf4j
public class ResortBookingServiceImpl implements ResortBookingService {

    private static final Set<String> ALLOWED_SORT_FIELDS = ResortBookingSortField.allowedFields();

    private final ResortBookingRepository resortBookingRepository;
    private final ResortRoomReservationService resortRoomReservationService;

    public ResortBookingServiceImpl(ResortBookingRepository resortBookingRepository,
                                    ResortRoomReservationService resortRoomReservationService) {
        this.resortBookingRepository = resortBookingRepository;
        this.resortRoomReservationService = resortRoomReservationService;
    }

    @Transactional
    @Override
    public SuccessResponse createPosBooking(CreateResortBookingRequest request,
                                            ResortEntity resortEntity,
                                            UserEntity userEntity,
                                            BookingSourceEntity bookingSourceEntity,
                                            List<CreateResortRoomReservationRequest> roomRequests,
                                            Map<Long, ResortRoomEntity> resortRoomEntityMap,
                                            Map<Long, ReservationStatusEntity> reservationStatusEntityMap,
                                            CurrencyEntity currencyEntity) {
        ResortBookingEntity entity = ResortBookingMapper.create(request);

        resortEntity.addResortBookingEntity(entity);
        userEntity.addResortBookingEntity(entity);
        bookingSourceEntity.addResortBookingEntity(entity);

        resortRoomReservationService.attachReservationEntities(roomRequests, entity, resortRoomEntityMap, reservationStatusEntityMap, currencyEntity);

        resortBookingRepository.save(entity);
        log.info("Booking created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public PaginatedResponse<ResortBookingDto> getAll(Long resortId, ResortBookingFilterRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ResortBookingDto> page = resortBookingRepository
                .findByResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false, pageable)
                .map(this::toFullDto);
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, Set.of());
    }

    /**
     * {@code reservations} chains in {@code ResortRoomReservationService#toDto} per active reservation (each
     * already carrying room/category/status/currency/price-unit/nightly-prices/guests); {@code totalPrice} is
     * the sum of those reservations' own totals, and {@code currency} is read off any one of them, since every
     * reservation in a booking is guaranteed to share one currency (see {@code ResortBookingService#createPosBooking}).
     */
    private ResortBookingDto toFullDto(ResortBookingEntity entity) {
        List<ResortRoomReservationDto> reservations = activeReservations(entity).stream()
                .map(resortRoomReservationService::toDto)
                .toList();

        BigDecimal totalPrice = reservations.stream()
                .map(ResortRoomReservationDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CurrencyDto currency = reservations.isEmpty() ? null : reservations.get(0).getCurrency();

        return ResortBookingMapper.toDto(entity)
                .bookingSource(BookingSourceMapper.toDto(entity.getBookingSourceEntity()).build())
                .reservations(reservations)
                .totalPrice(totalPrice)
                .currency(currency)
                .build();
    }

    private List<ResortRoomReservationEntity> activeReservations(ResortBookingEntity entity) {
        return entity.getResortRoomReservationEntities().stream()
                .filter(reservation -> Boolean.TRUE.equals(reservation.getIsActive())
                        && Boolean.FALSE.equals(reservation.getIsDeleted()))
                .sorted(Comparator.comparing(ResortRoomReservationEntity::getId))
                .toList();
    }
}
