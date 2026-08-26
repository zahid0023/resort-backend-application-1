package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryPriceEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryPriceRepository extends
        JpaRepository<@NonNull ResortRoomCategoryPriceEntity, @NonNull Long> {

    Optional<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long id, Boolean isActive, Boolean isDeleted);

    @Query("select distinct p.currencyEntity.code from ResortRoomCategoryPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.priceTypeEntity.code = :priceTypeCode "
            + "and p.isActive = :isActive and p.isDeleted = :isDeleted")
    List<String> findDistinctCurrencyCodeByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String priceTypeCode, Boolean isActive, Boolean isDeleted);

    List<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, Long currencyId, Boolean isActive, Boolean isDeleted);

    Optional<ResortRoomCategoryPriceEntity> findByResortRoomCategoryEntity_IdAndPriceTypeEntity_CodeAndCurrencyEntity_IdAndIsActiveAndIsDeleted(
            Long resortRoomCategoryId, String priceTypeCode, Long currencyId, Boolean isActive, Boolean isDeleted);

    /**
     * Same active-`BAS`-rows-per-room-category set {@code findDistinctCurrencyCodeBy...} reads, but with a
     * pessimistic write lock — used by {@code deleteByCurrency}'s "at least one currency must remain" guard so
     * two concurrent deletes of two *different* currencies for the same room category can't both read a stale
     * "still >1 currency" count and both proceed, leaving zero. Postgres re-checks each row's WHERE predicate
     * against its committed state once a blocked lock is granted, so a currency soft-deleted by a
     * just-committed concurrent call correctly drops out of the result.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ResortRoomCategoryPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.priceTypeEntity.code = 'BAS' "
            + "and p.isActive = true and p.isDeleted = false")
    List<ResortRoomCategoryPriceEntity> findActiveBasePricesForUpdate(Long resortRoomCategoryId);

    /**
     * Same single-currency active-`BAS` lookup {@code findByResortRoomCategoryEntity_IdAndPriceTypeEntity_Code
     * AndCurrencyEntity_IdAndIsActiveAndIsDeleted} does, but with a pessimistic write lock — used by
     * {@code createDateBoundPrice}'s "this currency must already have an active main price" check so it
     * contends on the same physical row {@link #findActiveBasePricesForUpdate} locks. Without this, a
     * `createHoliday`/`createSpecial` call and a concurrent `deleteByCurrency` call for the same currency could
     * each read a stale, independent snapshot — the create seeing an active BAS that the delete is
     * simultaneously in the middle of removing — leaving a freshly created HOLIDAY/SPECIAL row orphaned against
     * a currency with no base rate. With both sides locking the same row, one call blocks until the other
     * commits, then re-reads the post-commit state.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ResortRoomCategoryPriceEntity p "
            + "where p.resortRoomCategoryEntity.id = :resortRoomCategoryId "
            + "and p.priceTypeEntity.code = 'BAS' "
            + "and p.currencyEntity.id = :currencyId "
            + "and p.isActive = true and p.isDeleted = false")
    Optional<ResortRoomCategoryPriceEntity> findMainBasePriceForUpdate(Long resortRoomCategoryId, Long currencyId);
}
