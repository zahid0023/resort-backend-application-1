package com.example.resortbackendapplication1.reservation.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.CreateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.locale.UpdateReservationSourceLocaleRequest;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceLocaleDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationSourceLocaleMapper;
import com.example.resortbackendapplication1.reservation.repository.ReservationSourceLocaleRepository;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceLocaleService;
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
public class ReservationSourceLocaleServiceImpl implements ReservationSourceLocaleService {
    private final ReservationSourceLocaleRepository reservationSourceLocaleRepository;

    public ReservationSourceLocaleServiceImpl(ReservationSourceLocaleRepository reservationSourceLocaleRepository) {
        this.reservationSourceLocaleRepository = reservationSourceLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateReservationSourceLocaleRequest request,
                                  ReservationSourceEntity reservationSourceEntity,
                                  LocaleEntity localeEntity) {
        if (reservationSourceLocaleRepository.existsByReservationSourceEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                reservationSourceEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("ReservationSource already has a locale entry for locale id: " + localeEntity.getId());
        }

        ReservationSourceLocaleEntity entity = ReservationSourceLocaleMapper.create(request);
        reservationSourceEntity.addReservationSourceLocaleEntity(entity);
        localeEntity.addReservationSourceLocaleEntity(entity);
        reservationSourceLocaleRepository.save(entity);
        log.info("ReservationSourceLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(ReservationSourceLocaleEntity entity,
                                  UpdateReservationSourceLocaleRequest request) {
        ReservationSourceLocaleMapper.update(entity, request);
        reservationSourceLocaleRepository.save(entity);
        log.info("ReservationSourceLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ReservationSourceLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        reservationSourceLocaleRepository.save(entity);
        log.info("ReservationSourceLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ReservationSourceLocaleEntity getEntityById(Long reservationSourceId, Long id) {
        return reservationSourceLocaleRepository
                .findByReservationSourceEntity_IdAndIdAndIsActiveAndIsDeleted(reservationSourceId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ReservationSourceLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<ReservationSourceLocaleDto> getAll(Long reservationSourceId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull ReservationSourceLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? reservationSourceLocaleRepository.findByReservationSourceEntity_IdAndIsActiveAndIsDeleted(reservationSourceId, true, false, pageable)
                : reservationSourceLocaleRepository.findByReservationSourceEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        reservationSourceId, localeCode, true, false, pageable))
                .map(ReservationSourceLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long reservationSourceId) {
        List<String> codes = reservationSourceLocaleRepository
                .findLocaleEntity_CodeByReservationSourceEntity_IdAndIsActiveAndIsDeleted(reservationSourceId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
