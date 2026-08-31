package com.example.resortbackendapplication1.bookingsource.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.utils.Pagination;
import com.example.resortbackendapplication1.locale.dto.response.locales.LocaleCountResponse;
import com.example.resortbackendapplication1.locale.model.entity.LocaleEntity;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.CreateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.dto.request.bookingsource.locale.UpdateBookingSourceLocaleRequest;
import com.example.resortbackendapplication1.bookingsource.model.dto.BookingSourceLocaleDto;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceLocaleEntity;
import com.example.resortbackendapplication1.bookingsource.model.mapper.BookingSourceLocaleMapper;
import com.example.resortbackendapplication1.bookingsource.repository.BookingSourceLocaleRepository;
import com.example.resortbackendapplication1.bookingsource.service.BookingSourceLocaleService;
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
public class BookingSourceLocaleServiceImpl implements BookingSourceLocaleService {
    private final BookingSourceLocaleRepository bookingSourceLocaleRepository;

    public BookingSourceLocaleServiceImpl(BookingSourceLocaleRepository bookingSourceLocaleRepository) {
        this.bookingSourceLocaleRepository = bookingSourceLocaleRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateBookingSourceLocaleRequest request,
                                  BookingSourceEntity bookingSourceEntity,
                                  LocaleEntity localeEntity) {
        if (bookingSourceLocaleRepository.existsByBookingSourceEntity_IdAndLocaleEntity_IdAndIsActiveAndIsDeleted(
                bookingSourceEntity.getId(), localeEntity.getId(), true, false)) {
            throw new IllegalStateException("BookingSource already has a locale entry for locale id: " + localeEntity.getId());
        }

        BookingSourceLocaleEntity entity = BookingSourceLocaleMapper.create(request);
        bookingSourceEntity.addBookingSourceLocaleEntity(entity);
        localeEntity.addBookingSourceLocaleEntity(entity);
        bookingSourceLocaleRepository.save(entity);
        log.info("BookingSourceLocale created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse update(BookingSourceLocaleEntity entity,
                                  UpdateBookingSourceLocaleRequest request) {
        BookingSourceLocaleMapper.update(entity, request);
        bookingSourceLocaleRepository.save(entity);
        log.info("BookingSourceLocale updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(BookingSourceLocaleEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        bookingSourceLocaleRepository.save(entity);
        log.info("BookingSourceLocale soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public BookingSourceLocaleEntity getEntityById(Long bookingSourceId, Long id) {
        return bookingSourceLocaleRepository
                .findByBookingSourceEntity_IdAndIdAndIsActiveAndIsDeleted(bookingSourceId, id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("BookingSourceLocale not found with id: " + id));
    }

    @Override
    public PaginatedResponse<BookingSourceLocaleDto> getAll(Long bookingSourceId, String localeCode, PaginatedRequest paginatedRequest) {
        Pageable pageable = paginatedRequest.toPageable(Set.of());
        Page<@NonNull BookingSourceLocaleDto> dtoPage = (localeCode == null || localeCode.isBlank()
                ? bookingSourceLocaleRepository.findByBookingSourceEntity_IdAndIsActiveAndIsDeleted(bookingSourceId, true, false, pageable)
                : bookingSourceLocaleRepository.findByBookingSourceEntity_IdAndLocaleEntity_CodeContainingIgnoreCaseAndIsActiveAndIsDeleted(
                        bookingSourceId, localeCode, true, false, pageable))
                .map(BookingSourceLocaleMapper::toDto);
        return Pagination.buildPaginatedResponse(dtoPage);
    }

    @Override
    public LocaleCountResponse getCount(Long bookingSourceId) {
        List<String> codes = bookingSourceLocaleRepository
                .findLocaleEntity_CodeByBookingSourceEntity_IdAndIsActiveAndIsDeleted(bookingSourceId, true, false);
        return new LocaleCountResponse((long) codes.size(), codes);
    }
}
