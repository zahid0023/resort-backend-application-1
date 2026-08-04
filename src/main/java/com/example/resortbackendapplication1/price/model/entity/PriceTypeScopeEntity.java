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
@Table(name = "price_type_scopes")
public class PriceTypeScopeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "priceTypeScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceTypeScopeLocaleEntity> priceTypeScopeLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "priceTypeScopeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceTypeScopeAssignmentEntity> priceTypeScopeAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PriceTypeScope Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceTypeScopeLocaleEntity(PriceTypeScopeLocaleEntity entity) {
        addChild(priceTypeScopeLocaleEntities, entity, PriceTypeScopeLocaleEntity::assignPriceTypeScope, this);
    }

    public void removePriceTypeScopeLocaleEntity(PriceTypeScopeLocaleEntity entity) {
        removeChild(priceTypeScopeLocaleEntities, entity, (child, ignored) -> child.unassignPriceTypeScope());
    }

    // -------------------------------------------------------------------------
    // PriceTypeScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceTypeScopeAssignmentEntity(PriceTypeScopeAssignmentEntity entity) {
        addChild(priceTypeScopeAssignmentEntities, entity, PriceTypeScopeAssignmentEntity::assignPriceTypeScope, this);
    }

    public void removePriceTypeScopeAssignmentEntity(PriceTypeScopeAssignmentEntity entity) {
        removeChild(priceTypeScopeAssignmentEntities, entity, (child, ignored) -> child.unassignPriceTypeScope());
    }
}
