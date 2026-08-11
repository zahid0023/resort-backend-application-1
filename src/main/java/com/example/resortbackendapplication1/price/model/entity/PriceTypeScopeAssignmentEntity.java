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
@Table(name = "price_type_scope_assignments")
public class PriceTypeScopeAssignmentEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_scope_id", nullable = false)
    private PriceScopeEntity priceScopeEntity;

    /** Internal — call via {@link PriceScopeEntity#addPriceTypeScopeAssignmentEntity}. */
    public void assignPriceScope(PriceScopeEntity priceScopeEntity) {
        this.priceScopeEntity = priceScopeEntity;
    }

    /** Internal — call via {@link PriceScopeEntity#removePriceTypeScopeAssignmentEntity}. */
    public void unassignPriceScope() {
        this.priceScopeEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_type_id", nullable = false)
    private PriceTypeEntity priceTypeEntity;

    /** Internal — call via {@link PriceTypeEntity#addPriceTypeScopeAssignmentEntity}. */
    public void assignPriceType(PriceTypeEntity priceTypeEntity) {
        this.priceTypeEntity = priceTypeEntity;
    }

    /** Internal — call via {@link PriceTypeEntity#removePriceTypeScopeAssignmentEntity}. */
    public void unassignPriceType() {
        this.priceTypeEntity = null;
    }
}
