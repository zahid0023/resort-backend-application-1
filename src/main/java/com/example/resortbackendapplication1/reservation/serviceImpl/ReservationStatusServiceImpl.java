package com.example.resortbackendapplication1.reservation.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.CreateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.ReservationStatusFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationstatus.UpdateReservationStatusRequest;
import com.example.resortbackendapplication1.reservation.dto.response.reservationstatuses.ReservationStatusResponse;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationStatusDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusLocaleEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusLocaleMapper;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationStatusMapper;
import com.example.resortbackendapplication1.reservation.repository.ReservationStatusRepository;
import com.example.resortbackendapplication1.reservation.service.ReservationStatusService;
import com.example.resortbackendapplication1.reservation.specification.ReservationStatusSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class ReservationStatusServiceImpl implements ReservationStatusService {

    // No fields were classified as Filterable/Sortable in this entity, so both sets are empty; getAll still
    // works, it just always sorts by id and exposes no filter/sort options in the paginated response.
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ReservationStatusRepository reservationStatusRepository;

    public ReservationStatusServiceImpl(ReservationStatusRepository reservationStatusRepository) {
        this.reservationStatusRepository = reservationStatusRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateReservationStatusRequest request, LocaleEntity localeEntity) {
        if (reservationStatusRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("ReservationStatus with code '" + request.getCode() + "' already exists");
        }

        ReservationStatusEntity entity = ReservationStatusMapper.create(request);

        ReservationStatusLocaleEntity reservationStatusLocaleEntity = ReservationStatusLocaleMapper.create(request.getLocale());
        localeEntity.addReservationStatusLocaleEntity(reservationStatusLocaleEntity);

        entity.addReservationStatusLocaleEntity(reservationStatusLocaleEntity);

        reservationStatusRepository.save(entity);
        log.info("ReservationStatus created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ReservationStatusEntity getEntityById(Long id) {
        return reservationStatusRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ReservationStatus not found with id: " + id));
    }

    @Override
    public ReservationStatusResponse getById(Long id) {
        ReservationStatusEntity entity = getEntityById(id);
        ReservationStatusDto dto = ReservationStatusMapper.toDto(entity).build();
        return new ReservationStatusResponse(dto);
    }

    @Override
    public PaginatedResponse<ReservationStatusDto> getAll(ReservationStatusFilterRequest request) {
        Specification<@NonNull ReservationStatusEntity> specification =
                ReservationStatusSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ReservationStatusDto> page = reservationStatusRepository
                .findAll(specification, pageable)
                .map(entity -> ReservationStatusMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ReservationStatusEntity entity, UpdateReservationStatusRequest request) {
        ReservationStatusMapper.update(entity, request);
        reservationStatusRepository.save(entity);
        log.info("ReservationStatus updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ReservationStatusEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getReservationStatusLocaleEntities().forEach(reservationStatusLocaleEntity -> {
            reservationStatusLocaleEntity.setIsDeleted(true);
            reservationStatusLocaleEntity.setIsActive(false);
        });

        reservationStatusRepository.save(entity);
        log.info("ReservationStatus soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
