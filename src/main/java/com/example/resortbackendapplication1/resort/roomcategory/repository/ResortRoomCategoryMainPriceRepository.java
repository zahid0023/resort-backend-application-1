package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryMainPriceEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryMainPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategoryMainPriceEntity, @NonNull Long> {

    Optional<ResortRoomCategoryMainPriceEntity> findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long currencyId, Boolean isActive, Boolean isDeleted);

    @Query("select distinct p.currencyEntity.code from ResortRoomCategoryMainPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.isActive = :isActive and p.isDeleted = :isDeleted")
    List<String> findDistinctCurrencyCodeByResortRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Boolean isActive, Boolean isDeleted);

    /**
     * Same active main-price rows a room category has, but with a pessimistic write lock — used by
     * {@code deleteByCurrency}'s "at least one currency must remain" guard so two concurrent deletes of two
     * *different* currencies for the same room category can't both read a stale "still >1 currency" count and
     * both proceed, leaving zero. Postgres re-checks each row's WHERE predicate against its committed state
     * once a blocked lock is granted, so a currency soft-deleted by a just-committed concurrent call correctly
     * drops out of the result.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ResortRoomCategoryMainPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.isActive = true and p.isDeleted = false")
    List<ResortRoomCategoryMainPriceEntity> findActiveForUpdate(Long resortRoomCategoryId);

    /**
     * Same single-currency active main-price lookup {@code findByResortRoomCategoryEntity_IdAndCurrencyEntity_Id
     * AndIsActiveAndIsDeleted} does, but with a pessimistic write lock — used by the "this currency must
     * already have an active main price" check on special creation, so it contends on the same physical row
     * {@link #findActiveForUpdate} locks. Without this, a createSpecial call and a concurrent deleteByCurrency
     * call for the same currency could each read a stale, independent snapshot — the create seeing an active
     * main price that the delete is simultaneously in the middle of removing — leaving a freshly created
     * special row orphaned against a currency with no base rate. With both sides locking the same row, one
     * call blocks until the other commits, then re-reads the post-commit state.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ResortRoomCategoryMainPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.currencyEntity.id = :currencyId "
            + "and p.isActive = true and p.isDeleted = false")
    Optional<ResortRoomCategoryMainPriceEntity> findForUpdate(Long resortRoomCategoryId, Long currencyId);
}
