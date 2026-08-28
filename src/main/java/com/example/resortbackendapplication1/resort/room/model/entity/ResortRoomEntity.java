package com.example.resortbackendapplication1.resort.room.model.entity;
import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
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
@Table(name = "resort_rooms")
public class ResortRoomEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_status_id", nullable = false)
    private RoomStatusEntity roomStatusEntity;

    /** Internal — call via {@link RoomStatusEntity#addResortRoomEntity}. */
    public void assignRoomStatus(RoomStatusEntity roomStatusEntity) {
        this.roomStatusEntity = roomStatusEntity;
    }

    /** Internal — call via {@link RoomStatusEntity#removeResortRoomEntity}. */
    public void unassignRoomStatus() {
        this.roomStatusEntity = null;
    }

    /**
     * Resort-scoped identifier, unique per resort. Uniqueness spans the whole resort (reached via
     * resortRoomCategoryEntity.resortEntity), which a plain DB index can't express, so it's enforced by the
     * fn_validate_resort_room_code_unique_per_resort trigger in addition to the application-level check here.
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** Free-form physical location, both optional — not a fixed vocabulary, so no lookup table. */
    @Column(name = "floor_number")
    private Integer floorNumber;

    @Size(max = 100)
    @Column(name = "building", length = 100)
    private String building;

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomLocaleEntity> resortRoomLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomLocaleEntity(ResortRoomLocaleEntity entity) {
        addChild(resortRoomLocaleEntities, entity, ResortRoomLocaleEntity::assignResortRoom, this);
    }

    public void removeResortRoomLocaleEntity(ResortRoomLocaleEntity entity) {
        removeChild(resortRoomLocaleEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "resortRoomEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResortRoomMetaEntity resortRoomMetaEntity;

    // -------------------------------------------------------------------------
    // ResortRoomMeta relationship helpers (one-to-one — every resort room has
    // at most one meta override row)
    // -------------------------------------------------------------------------

    public void assignResortRoomMetaEntity(ResortRoomMetaEntity entity) {
        this.resortRoomMetaEntity = entity;
        entity.assignResortRoom(this);
    }

    public void unassignResortRoomMetaEntity() {
        if (this.resortRoomMetaEntity != null) {
            this.resortRoomMetaEntity.assignResortRoom(null);
        }
        this.resortRoomMetaEntity = null;
    }

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomBedEntity> resortRoomBedEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomBed relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomBedEntity(ResortRoomBedEntity entity) {
        addChild(resortRoomBedEntities, entity, ResortRoomBedEntity::assignResortRoom, this);
    }

    public void removeResortRoomBedEntity(ResortRoomBedEntity entity) {
        removeChild(resortRoomBedEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomFacilityGroupEntity> resortRoomFacilityGroupEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomFacilityGroup relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityGroupEntity(ResortRoomFacilityGroupEntity entity) {
        addChild(resortRoomFacilityGroupEntities, entity, ResortRoomFacilityGroupEntity::assignResortRoom, this);
    }

    public void removeResortRoomFacilityGroupEntity(ResortRoomFacilityGroupEntity entity) {
        removeChild(resortRoomFacilityGroupEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomFacilityEntity> resortRoomFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityEntity(ResortRoomFacilityEntity entity) {
        addChild(resortRoomFacilityEntities, entity, ResortRoomFacilityEntity::assignResortRoom, this);
    }

    public void removeResortRoomFacilityEntity(ResortRoomFacilityEntity entity) {
        removeChild(resortRoomFacilityEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomMainPriceEntity> resortRoomMainPriceEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomMainPrice relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomMainPriceEntity(ResortRoomMainPriceEntity entity) {
        addChild(resortRoomMainPriceEntities, entity, ResortRoomMainPriceEntity::assignResortRoom, this);
    }

    public void removeResortRoomMainPriceEntity(ResortRoomMainPriceEntity entity) {
        removeChild(resortRoomMainPriceEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }

    @OneToMany(mappedBy = "resortRoomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomSpecialPriceEntity> resortRoomSpecialPriceEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomSpecialPrice relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomSpecialPriceEntity(ResortRoomSpecialPriceEntity entity) {
        addChild(resortRoomSpecialPriceEntities, entity, ResortRoomSpecialPriceEntity::assignResortRoom, this);
    }

    public void removeResortRoomSpecialPriceEntity(ResortRoomSpecialPriceEntity entity) {
        removeChild(resortRoomSpecialPriceEntities, entity, (child, ignored) -> child.unassignResortRoom());
    }
}
