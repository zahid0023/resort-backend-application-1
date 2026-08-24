package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resort_room_categories")
public class ResortRoomCategoryEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortRoomCategoryEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortRoomCategoryEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_category_id", nullable = false)
    private RoomCategoryEntity roomCategoryEntity;

    /** Internal — call via {@link RoomCategoryEntity#addResortRoomCategoryEntity}. */
    public void assignRoomCategory(RoomCategoryEntity roomCategoryEntity) {
        this.roomCategoryEntity = roomCategoryEntity;
    }

    /** Internal — call via {@link RoomCategoryEntity#removeResortRoomCategoryEntity}. */
    public void unassignRoomCategory() {
        this.roomCategoryEntity = null;
    }

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryLocaleEntity> resortRoomCategoryLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryLocaleEntity(ResortRoomCategoryLocaleEntity entity) {
        addChild(resortRoomCategoryLocaleEntities, entity, ResortRoomCategoryLocaleEntity::assignResortRoomCategory, this);
    }

    public void removeResortRoomCategoryLocaleEntity(ResortRoomCategoryLocaleEntity entity) {
        removeChild(resortRoomCategoryLocaleEntities, entity, (child, ignored) -> child.unassignResortRoomCategory());
    }

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "resortRoomCategoryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResortRoomCategoryMetaEntity resortRoomCategoryMetaEntity;

    // -------------------------------------------------------------------------
    // ResortRoomCategoryMeta relationship helpers (one-to-one — every resort
    // room category has at most one meta)
    // -------------------------------------------------------------------------

    public void assignResortRoomCategoryMetaEntity(ResortRoomCategoryMetaEntity entity) {
        this.resortRoomCategoryMetaEntity = entity;
        entity.assignResortRoomCategory(this);
    }

    public void unassignResortRoomCategoryMetaEntity() {
        if (this.resortRoomCategoryMetaEntity != null) {
            this.resortRoomCategoryMetaEntity.assignResortRoomCategory(null);
        }
        this.resortRoomCategoryMetaEntity = null;
    }

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryBedEntity> resortRoomCategoryBedEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryBed relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryBedEntity(ResortRoomCategoryBedEntity entity) {
        addChild(resortRoomCategoryBedEntities, entity, ResortRoomCategoryBedEntity::assignResortRoomCategory, this);
    }

    public void removeResortRoomCategoryBedEntity(ResortRoomCategoryBedEntity entity) {
        removeChild(resortRoomCategoryBedEntities, entity, (child, ignored) -> child.unassignResortRoomCategory());
    }

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryFacilityGroupEntity> resortRoomCategoryFacilityGroupEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacilityGroup relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityGroupEntity(ResortRoomCategoryFacilityGroupEntity entity) {
        addChild(resortRoomCategoryFacilityGroupEntities, entity, ResortRoomCategoryFacilityGroupEntity::assignResortRoomCategory, this);
    }

    public void removeResortRoomCategoryFacilityGroupEntity(ResortRoomCategoryFacilityGroupEntity entity) {
        removeChild(resortRoomCategoryFacilityGroupEntities, entity, (child, ignored) -> child.unassignResortRoomCategory());
    }

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryFacilityEntity> resortRoomCategoryFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityEntity(ResortRoomCategoryFacilityEntity entity) {
        addChild(resortRoomCategoryFacilityEntities, entity, ResortRoomCategoryFacilityEntity::assignResortRoomCategory, this);
    }

    public void removeResortRoomCategoryFacilityEntity(ResortRoomCategoryFacilityEntity entity) {
        removeChild(resortRoomCategoryFacilityEntities, entity, (child, ignored) -> child.unassignResortRoomCategory());
    }

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryPriceEntity> resortRoomCategoryPriceEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryPrice relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryPriceEntity(ResortRoomCategoryPriceEntity entity) {
        addChild(resortRoomCategoryPriceEntities, entity, ResortRoomCategoryPriceEntity::assignResortRoomCategory, this);
    }

    public void removeResortRoomCategoryPriceEntity(ResortRoomCategoryPriceEntity entity) {
        removeChild(resortRoomCategoryPriceEntities, entity, (child, ignored) -> child.unassignResortRoomCategory());
    }
}
