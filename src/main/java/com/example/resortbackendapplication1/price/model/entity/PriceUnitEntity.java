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
@Table(name = "price_units")
public class PriceUnitEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "priceUnitEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceUnitLocaleEntity> priceUnitLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "priceUnitEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriceUnitScopeAssignmentEntity> priceUnitScopeAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PriceUnit Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceUnitLocaleEntity(PriceUnitLocaleEntity entity) {
        addChild(priceUnitLocaleEntities, entity, PriceUnitLocaleEntity::assignPriceUnit, this);
    }

    public void removePriceUnitLocaleEntity(PriceUnitLocaleEntity entity) {
        removeChild(priceUnitLocaleEntities, entity, (child, ignored) -> child.unassignPriceUnit());
    }

    // -------------------------------------------------------------------------
    // PriceUnitScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addPriceUnitScopeAssignmentEntity(PriceUnitScopeAssignmentEntity entity) {
        addChild(priceUnitScopeAssignmentEntities, entity, PriceUnitScopeAssignmentEntity::assignPriceUnit, this);
    }

    public void removePriceUnitScopeAssignmentEntity(PriceUnitScopeAssignmentEntity entity) {
        removeChild(priceUnitScopeAssignmentEntities, entity, (child, ignored) -> child.unassignPriceUnit());
    }
}
