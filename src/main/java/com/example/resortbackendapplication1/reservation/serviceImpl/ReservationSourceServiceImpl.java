package com.example.resortbackendapplication1.reservation.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.CreateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.ReservationSourceFilterRequest;
import com.example.resortbackendapplication1.reservation.dto.request.reservationsource.UpdateReservationSourceRequest;
import com.example.resortbackendapplication1.reservation.dto.response.reservationsources.ReservationSourceResponse;
import com.example.resortbackendapplication1.reservation.model.dto.ReservationSourceDto;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceLocaleEntity;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationSourceLocaleMapper;
import com.example.resortbackendapplication1.reservation.model.mapper.ReservationSourceMapper;
import com.example.resortbackendapplication1.reservation.repository.ReservationSourceRepository;
import com.example.resortbackendapplication1.reservation.service.ReservationSourceService;
import com.example.resortbackendapplication1.reservation.specification.ReservationSourceSpecification;
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
public class ReservationSourceServiceImpl implements ReservationSourceService {

    // No fields were classified as Filterable/Sortable in this entity, so both sets are empty; getAll still
    // works, it just always sorts by id and exposes no filter/sort options in the paginated response.
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final ReservationSourceRepository reservationSourceRepository;

    public ReservationSourceServiceImpl(ReservationSourceRepository reservationSourceRepository) {
        this.reservationSourceRepository = reservationSourceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateReservationSourceRequest request, LocaleEntity localeEntity) {
        if (reservationSourceRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("ReservationSource with code '" + request.getCode() + "' already exists");
        }

        ReservationSourceEntity entity = ReservationSourceMapper.create(request);

        ReservationSourceLocaleEntity reservationSourceLocaleEntity = ReservationSourceLocaleMapper.create(request.getLocale());
        localeEntity.addReservationSourceLocaleEntity(reservationSourceLocaleEntity);

        entity.addReservationSourceLocaleEntity(reservationSourceLocaleEntity);

        reservationSourceRepository.save(entity);
        log.info("ReservationSource created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public ReservationSourceEntity getEntityById(Long id) {
        return reservationSourceRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("ReservationSource not found with id: " + id));
    }

    @Override
    public ReservationSourceResponse getById(Long id) {
        ReservationSourceEntity entity = getEntityById(id);
        ReservationSourceDto dto = ReservationSourceMapper.toDto(entity).build();
        return new ReservationSourceResponse(dto);
    }

    @Override
    public PaginatedResponse<ReservationSourceDto> getAll(ReservationSourceFilterRequest request) {
        Specification<@NonNull ReservationSourceEntity> specification =
                ReservationSourceSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull ReservationSourceDto> page = reservationSourceRepository
                .findAll(specification, pageable)
                .map(entity -> ReservationSourceMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(ReservationSourceEntity entity, UpdateReservationSourceRequest request) {
        ReservationSourceMapper.update(entity, request);
        reservationSourceRepository.save(entity);
        log.info("ReservationSource updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(ReservationSourceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getReservationSourceLocaleEntities().forEach(reservationSourceLocaleEntity -> {
            reservationSourceLocaleEntity.setIsDeleted(true);
            reservationSourceLocaleEntity.setIsActive(false);
        });

        reservationSourceRepository.save(entity);
        log.info("ReservationSource soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
