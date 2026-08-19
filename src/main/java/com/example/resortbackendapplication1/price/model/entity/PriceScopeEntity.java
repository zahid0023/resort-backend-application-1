package com.example.resortbackendapplication1.price.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "price_scopes")
public class PriceScopeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "priceScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceScopeLocaleEntity> priceScopeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "priceScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceTypeScopeAssignmentEntity> priceTypeScopeAssignmentEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "priceScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceUnitScopeAssignmentEntity> priceUnitScopeAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PriceScope Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceScopeLocaleEntity(PriceScopeLocaleEntity entity) {
        addChild(priceScopeLocaleEntities, entity, PriceScopeLocaleEntity::assignPriceScope, this);
    }

    public void removePriceScopeLocaleEntity(PriceScopeLocaleEntity entity) {
        removeChild(priceScopeLocaleEntities, entity, (child, ignored) -> child.unassignPriceScope());
    }

    // -------------------------------------------------------------------------
    // PriceTypeScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceTypeScopeAssignmentEntity(PriceTypeScopeAssignmentEntity entity) {
        addChild(priceTypeScopeAssignmentEntities, entity, PriceTypeScopeAssignmentEntity::assignPriceScope, this);
    }

    public void removePriceTypeScopeAssignmentEntity(PriceTypeScopeAssignmentEntity entity) {
        removeChild(priceTypeScopeAssignmentEntities, entity, (child, ignored) -> child.unassignPriceScope());
    }

    // -------------------------------------------------------------------------
    // PriceUnitScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceUnitScopeAssignmentEntity(PriceUnitScopeAssignmentEntity entity) {
        addChild(priceUnitScopeAssignmentEntities, entity, PriceUnitScopeAssignmentEntity::assignPriceScope, this);
    }

    public void removePriceUnitScopeAssignmentEntity(PriceUnitScopeAssignmentEntity entity) {
        removeChild(priceUnitScopeAssignmentEntities, entity, (child, ignored) -> child.unassignPriceScope());
    }
}
