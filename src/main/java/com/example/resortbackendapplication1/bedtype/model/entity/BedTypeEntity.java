package com.example.resortbackendapplication1.bedtype.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryBedEntity;
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
@Table(name = "bed_types")
public class BedTypeEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "bedTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BedTypeLocaleEntity> bedTypeLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // BedType Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addBedTypeLocaleEntity(BedTypeLocaleEntity entity) {
        addChild(bedTypeLocaleEntities, entity, BedTypeLocaleEntity::assignBedType, this);
    }

    public void removeBedTypeLocaleEntity(BedTypeLocaleEntity entity) {
        removeChild(bedTypeLocaleEntities, entity, (child, ignored) -> child.unassignBedType());
    }

    @OneToMany(mappedBy = "bedTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryBedEntity> resortRoomCategoryBedEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryBed relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryBedEntity(ResortRoomCategoryBedEntity entity) {
        addChild(resortRoomCategoryBedEntities, entity, ResortRoomCategoryBedEntity::assignBedType, this);
    }

    public void removeResortRoomCategoryBedEntity(ResortRoomCategoryBedEntity entity) {
        removeChild(resortRoomCategoryBedEntities, entity, (child, ignored) -> child.unassignBedType());
    }
}
