package com.example.resortbackendapplication1.resort.roomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_main_prices")
public class ResortRoomCategoryMainPriceEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategoryMainPriceEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategoryMainPriceEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    /** Billing unit shared by base/weekday/weekend (PER_NIGHT/PER_DAY/PER_PERSON/...). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_unit_id", nullable = false)
    private PriceUnitEntity priceUnitEntity;

    /** Currency of the three prices below. Immutable after creation. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    /** Default rack rate. Enforced >= 0 by the DB; weekdayPrice/weekendPrice cannot exceed it. */
    @NotNull
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    /** Normal weekday rate. Cannot exceed {@link #basePrice}, enforced by the DB. */
    @NotNull
    @Column(name = "weekday_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekdayPrice;

    /** Normal weekend rate. Cannot exceed {@link #basePrice}, enforced by the DB. */
    @NotNull
    @Column(name = "weekend_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekendPrice;
}
