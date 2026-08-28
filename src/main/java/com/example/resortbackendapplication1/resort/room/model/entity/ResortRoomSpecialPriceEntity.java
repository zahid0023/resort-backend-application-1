package com.example.resortbackendapplication1.resort.room.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A resort room's per-currency SPECIAL price override. Requires the room to already have an active
 * {@link ResortRoomMainPriceEntity} for the same currency — a room can't override a special rate without
 * first overriding the base rate it layers on top of.
 */
@Getter
@Setter
@Entity
@Table(name = "resort_room_special_prices")
public class ResortRoomSpecialPriceEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addResortRoomSpecialPriceEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeResortRoomSpecialPriceEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    /** Billing unit shared by weekdayPrice/weekendPrice (PER_NIGHT/PER_DAY/PER_PERSON/...). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_unit_id", nullable = false)
    private PriceUnitEntity priceUnitEntity;

    /** Currency of the two prices below. Immutable after creation. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    @NotBlank
    @Size(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    /** Applies on weekday dates within [validFrom, validTo]. No cap vs. the room's base price. */
    @NotNull
    @Column(name = "weekday_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekdayPrice;

    /** Applies on weekend dates within [validFrom, validTo]. No cap vs. the room's base price. */
    @NotNull
    @Column(name = "weekend_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekendPrice;

    /** Tie-breaker when multiple SPECIAL rules could apply to the same date — higher wins. */
    @NotNull
    @ColumnDefault("0")
    @Column(name = "priority", nullable = false)
    private Integer priority = 0;
}
