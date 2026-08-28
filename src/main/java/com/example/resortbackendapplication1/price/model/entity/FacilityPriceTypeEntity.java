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
@Table(name = "facility_price_types")
public class FacilityPriceTypeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "facilityPriceTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityPriceTypeLocaleEntity> facilityPriceTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // FacilityPriceType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityPriceTypeLocaleEntity(FacilityPriceTypeLocaleEntity entity) {
        addChild(facilityPriceTypeLocaleEntities, entity, FacilityPriceTypeLocaleEntity::assignFacilityPriceType, this);
    }

    public void removeFacilityPriceTypeLocaleEntity(FacilityPriceTypeLocaleEntity entity) {
        removeChild(facilityPriceTypeLocaleEntities, entity, (child, ignored) -> child.unassignFacilityPriceType());
    }
}
