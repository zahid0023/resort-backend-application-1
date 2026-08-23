package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
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
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "resort_facility_prices")
public class ResortFacilityPriceEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_facility_id", nullable = false)
    private ResortFacilityEntity resortFacilityEntity;

    /** Internal — call via {@link ResortFacilityEntity#addResortFacilityPriceEntity}. */
    public void assignResortFacility(ResortFacilityEntity resortFacilityEntity) {
        this.resortFacilityEntity = resortFacilityEntity;
    }

    /** Internal — call via {@link ResortFacilityEntity#removeResortFacilityPriceEntity}. */
    public void unassignResortFacility() {
        this.resortFacilityEntity = null;
    }

    /** Pricing classification (FREE/INCLUDED/FIXED/VARIABLE). Immutable after creation. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_type_id", nullable = false)
    private PriceTypeEntity priceTypeEntity;

    /** Billing unit (PER_PERSON/PER_ROOM/...). Null for FREE/INCLUDED pricing. Immutable after creation. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_unit_id")
    private PriceUnitEntity priceUnitEntity;

    /** Currency of {@code amount}. Immutable after creation. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    /** Monetary amount. Null for FREE/INCLUDED pricing types. */
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "note", nullable = false, columnDefinition = "text")
    private String note = "";
}
