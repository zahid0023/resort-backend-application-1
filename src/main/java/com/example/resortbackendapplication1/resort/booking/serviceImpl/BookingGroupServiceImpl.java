package com.example.resortbackendapplication1.resort.booking.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.currency.model.mapper.CurrencyMapper;
import com.example.resortbackendapplication1.price.model.mapper.PriceUnitMapper;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationSourceMapper;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusMapper;
import com.example.resortbackendapplication1.resort.booking.dto.response.bookinggroups.BookingGroupResponse;
import com.example.resortbackendapplication1.resort.booking.model.dto.BookingGroupDto;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.booking.repository.BookingGroupRepository;
import com.example.resortbackendapplication1.resort.booking.service.BookingGroupService;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.reservation.model.dto.ReservationDto;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import com.example.resortbackendapplication1.resort.reservation.model.mapper.ReservationMapper;
import com.example.resortbackendapplication1.resort.room.model.mapper.ResortRoomMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BookingGroupServiceImpl implements BookingGroupService {

    private final BookingGroupRepository bookingGroupRepository;

    public BookingGroupServiceImpl(BookingGroupRepository bookingGroupRepository) {
        this.bookingGroupRepository = bookingGroupRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(ResortEntity resortEntity, UserEntity customerEntity) {
        BookingGroupEntity entity = new BookingGroupEntity();
        entity.setResortEntity(resortEntity);
        entity.setUserEntity(customerEntity);
        bookingGroupRepository.save(entity);
        log.info("BookingGroup created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BookingGroupEntity getEntityById(Long resortId, Long id) {
        return bookingGroupRepository.findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(resortId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BookingGroup not found with id: " + id));
    }

    @Override
    public BookingGroupResponse getById(Long resortId, Long id) {
        BookingGroupEntity entity = getEntityById(resortId, id);
        BookingGroupDto dto = BookingGroupDto.builder()
                .id(entity.getId())
                .customerId(entity.getUserEntity().getId())
                .reservations(entity.getReservationEntities().stream()
                        .filter(reservation -> Boolean.TRUE.equals(reservation.getIsActive())
                                && Boolean.FALSE.equals(reservation.getIsDeleted()))
                        .map(this::toFullReservationDto)
                        .toList())
                .build();
        return new BookingGroupResponse(dto);
    }

    /** Mirrors ReservationServiceImpl#toFullDto exactly — a ServiceImpl must never call another entity's Service. */
    private ReservationDto toFullReservationDto(ReservationEntity entity) {
        return ReservationMapper.toDto(entity)
                .resortRoom(ResortRoomMapper.toDto(entity.getResortRoomEntity()).build())
                .reservationStatus(ReservationStatusMapper.toDto(entity.getReservationStatusEntity()).build())
                .reservationSource(ReservationSourceMapper.toDto(entity.getReservationSourceEntity()).build())
                .currency(CurrencyMapper.toDto(entity.getCurrencyEntity()).build())
                .priceUnit(PriceUnitMapper.toDto(entity.getPriceUnitEntity()).build())
                .build();
    }
}
