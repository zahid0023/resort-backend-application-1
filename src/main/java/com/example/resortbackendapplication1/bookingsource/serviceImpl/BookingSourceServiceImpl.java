package com.example.resortbackendapplication1.bookingsource.serviceImpl;

import com.example.resortbackendapplication1.commons.context.LocaleContext;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.CreateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.BookingSourceFilterRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.UpdateBookingSourceRequest;
import com.example.resortbackendapplication1.bookingsource.dto.response.bookingsources.BookingSourceResponse;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import com.example.resortbackendapplication1.bookingsource.model.mapper.BookingSourceLocaleMapper;
import com.example.resortbackendapplication1.bookingsource.model.mapper.BookingSourceMapper;
import com.example.resortbackendapplication1.bookingsource.repository.BookingSourceRepository;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceService;
import com.example.resortbackendapplication1.bookingsource.specification.BookingSourceSpecification;
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
public class BookingSourceServiceImpl implements BookingSourceService {

    // No fields were classified as Filterable/Sortable in this entity, so both sets are empty; getAll still
    // works, it just always sorts by id and exposes no filter/sort options in the paginated response.
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of();
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of();

    private final BookingSourceRepository bookingSourceRepository;

    public BookingSourceServiceImpl(BookingSourceRepository bookingSourceRepository) {
        this.bookingSourceRepository = bookingSourceRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateBookingSourceRequest request, LocaleEntity localeEntity) {
        if (bookingSourceRepository.existsByCodeAndIsActiveAndIsDeleted(request.getCode(), true, false)) {
            throw new IllegalStateException("BookingSource with code '" + request.getCode() + "' already exists");
        }

        BookingSourceEntity entity = BookingSourceMapper.create(request);

        BookingSourceLocaleEntity bookingSourceLocaleEntity = BookingSourceLocaleMapper.create(request.getLocale());
        localeEntity.addBookingSourceLocaleEntity(bookingSourceLocaleEntity);

        entity.addBookingSourceLocaleEntity(bookingSourceLocaleEntity);

        bookingSourceRepository.save(entity);
        log.info("BookingSource created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BookingSourceEntity getEntityById(Long id) {
        return bookingSourceRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BookingSource not found with id: " + id));
    }

    @Override
    public BookingSourceResponse getById(Long id) {
        BookingSourceEntity entity = getEntityById(id);
        BookingSourceDto dto = BookingSourceMapper.toDto(entity).build();
        return new BookingSourceResponse(dto);
    }

    @Override
    public PaginatedResponse<BookingSourceDto> getAll(BookingSourceFilterRequest request) {
        Specification<@NonNull BookingSourceEntity> specification =
                BookingSourceSpecification.filter(request, LocaleContext.getLocaleId());
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<@NonNull BookingSourceDto> page = bookingSourceRepository
                .findAll(specification, pageable)
                .map(entity -> BookingSourceMapper.toDto(entity).build());
        return Pagination.buildPaginatedResponse(page, ALLOWED_SORT_FIELDS, ALLOWED_SEARCH_FIELDS);
    }

    @Transactional
    @Override
    public SuccessResponse update(BookingSourceEntity entity, UpdateBookingSourceRequest request) {
        BookingSourceMapper.update(entity, request);
        bookingSourceRepository.save(entity);
        log.info("BookingSource updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(BookingSourceEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);

        entity.getBookingSourceLocaleEntities().forEach(bookingSourceLocaleEntity -> {
            bookingSourceLocaleEntity.setIsDeleted(true);
            bookingSourceLocaleEntity.setIsActive(false);
        });

        bookingSourceRepository.save(entity);
        log.info("BookingSource soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
