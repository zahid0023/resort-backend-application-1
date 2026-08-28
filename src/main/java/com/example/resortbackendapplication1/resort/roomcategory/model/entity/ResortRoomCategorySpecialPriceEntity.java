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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_special_prices")
public class ResortRoomCategorySpecialPriceEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategorySpecialPriceEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategorySpecialPriceEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
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

    /** Applies on weekday dates within [validFrom, validTo]. No cap vs. the room category's base price. */
    @NotNull
    @Column(name = "weekday_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekdayPrice;

    /** Applies on weekend dates within [validFrom, validTo]. No cap vs. the room category's base price. */
    @NotNull
    @Column(name = "weekend_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal weekendPrice;

    /** Tie-breaker when multiple SPECIAL rules could apply to the same date — higher wins. */
    @NotNull
    @ColumnDefault("0")
    @Column(name = "priority", nullable = false)
    private Integer priority = 0;
}
