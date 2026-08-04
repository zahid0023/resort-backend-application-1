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
    @JoinColumn(name = "price_type_scope_id", nullable = false)
    private PriceTypeScopeEntity priceTypeScopeEntity;

    /** Internal — call via {@link PriceTypeScopeEntity#addPriceTypeScopeAssignmentEntity}. */
    public void assignPriceTypeScope(PriceTypeScopeEntity priceTypeScopeEntity) {
        this.priceTypeScopeEntity = priceTypeScopeEntity;
    }

    /** Internal — call via {@link PriceTypeScopeEntity#removePriceTypeScopeAssignmentEntity}. */
    public void unassignPriceTypeScope() {
        this.priceTypeScopeEntity = null;
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
