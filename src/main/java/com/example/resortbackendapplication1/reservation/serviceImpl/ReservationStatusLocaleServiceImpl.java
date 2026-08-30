package com.example.resortbackendapplication1.reservation.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.CreateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.locale.UpdateReservationStatusLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusLocaleMapper;
import com.example.resortbackendapplication1.reservation.repository.ReservationStatusLocaleRepository;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusLocaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ReservationStatusLocaleServiceImpl implements ReservationStatusLocaleService {
    private final ReservationStatusLocaleRepository reservationStatusLocaleRepository;

    public ReservationStatusLocaleServiceImpl(ReservationStatusLocaleRepository reservationStatusLocaleRepository) {
        this.reservationStatusLocaleRepository = reservationStatusLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateReservationStatusLocaleRequest request,
                                  ReservationStatusEntity reservationStatusEntity,
                                  LocaleEntity localeEntity) {
        if (reservationStatusLocaleRepository.existsByReservationStatusEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                reservationStatusEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ReservationStatus already has a locale entry for locale id: " + localeEntity.getId());
        }

        ReservationStatusLocaleEntity entity = ReservationStatusLocaleMapper.create(request);
        reservationStatusEntity.addReservationStatusLocaleEntity(entity);
        localeEntity.addReservationStatusLocaleEntity(entity);
        reservationStatusLocaleRepository.save(entity);
        log.info("ReservationStatusLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ReservationStatusLocaleEntity entity,
                                  UpdateReservationStatusLocaleRequest request) {
        ReservationStatusLocaleMapper.update(entity, request);
        reservationStatusLocaleRepository.save(entity);
        log.info("ReservationStatusLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ReservationStatusLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        reservationStatusLocaleRepository.save(entity);
        log.info("ReservationStatusLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ReservationStatusLocaleEntity getEntityById(Long reservationStatusId, Long id) {
        return reservationStatusLocaleRepository
                .findByReservationStatusEntity_IdAndIdAndIsActiveAndIsDeleted(reservationStatusId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ReservationStatusLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ReservationStatusLocaleDto> getAll(Long reservationStatusId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ReservationStatusLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? reservationStatusLocaleRepository.findByReservationStatusEntity_IdAndIsActiveAndIsDeleted(reservationStatusId, true, false, pageable)
                : reservationStatusLocaleRepository.findByReservationStatusEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        reservationStatusId, localeCode, true, false, pageable))
                .map(ReservationStatusLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long reservationStatusId) {
        List<String> codes = reservationStatusLocaleRepository
                .findLocaleEntity_CodeByReservationStatusEntity_IdAndIsActiveAndIsDeleted(reservationStatusId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
