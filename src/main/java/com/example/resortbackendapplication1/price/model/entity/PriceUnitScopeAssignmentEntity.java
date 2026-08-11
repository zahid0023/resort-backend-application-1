package com.example.resortbackendapplication1.price.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "price_unit_scope_assignments")
public class PriceUnitScopeAssignmentEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_scope_id", nullable = false)
    private PriceScopeEntity priceScopeEntity;

    /** Internal — call via {@link PriceScopeEntity#addPriceUnitScopeAssignmentEntity}. */
    public void assignPriceScope(PriceScopeEntity priceScopeEntity) {
        this.priceScopeEntity = priceScopeEntity;
    }

    /** Internal — call via {@link PriceScopeEntity#removePriceUnitScopeAssignmentEntity}. */
    public void unassignPriceScope() {
        this.priceScopeEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_unit_id", nullable = false)
    private PriceUnitEntity priceUnitEntity;

    /** Internal — call via {@link PriceUnitEntity#addPriceUnitScopeAssignmentEntity}. */
    public void assignPriceUnit(PriceUnitEntity priceUnitEntity) {
        this.priceUnitEntity = priceUnitEntity;
    }

    /** Internal — call via {@link PriceUnitEntity#removePriceUnitScopeAssignmentEntity}. */
    public void unassignPriceUnit() {
        this.priceUnitEntity = null;
    }
}
