package com.example.resortbackendapplication1.resort.room.repository;

import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomMainPriceEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomMainPriceRepository extends
        JpaRepository<@NonNull ResortRoomMainPriceEntity, @NonNull Long> {

    Optional<ResortRoomMainPriceEntity> findByResortRoomEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Long currencyId, Boolean isActive, Boolean isDeleted);

    @Query("select distinct p.currencyEntity.code from ResortRoomMainPriceEntity p "
            + "where p.resortRoomEntity.id = :resortRoomId "
            + "and p.isActive = :isActive and p.isDeleted = :isDeleted")
    List<String> findDistinctCurrencyCodeByResortRoomEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomId, Boolean isActive, Boolean isDeleted);

    /**
     * Same single-currency active override lookup {@code findByResortRoomEntity_IdAndCurrencyEntity_Id
     * AndIsActiveAndIsDeleted} does, but with a pessimistic write lock — used by the "this currency must
     * already have an active room-level main price" check on special override creation, so it
     * contends on the same physical row a concurrent {@code deleteByCurrency} call locks. Mirrors
     * {@code ResortRoomCategoryMainPriceRepository.findForUpdate}.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ResortRoomMainPriceEntity p "
            + "where p.resortRoomEntity.id = :resortRoomId "
            + "and p.currencyEntity.id = :currencyId "
            + "and p.isActive = true and p.isDeleted = false")
    Optional<ResortRoomMainPriceEntity> findForUpdate(Long resortRoomId, Long currencyId);
}
