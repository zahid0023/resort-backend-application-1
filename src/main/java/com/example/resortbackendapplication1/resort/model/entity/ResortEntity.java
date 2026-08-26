package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resorts")
public class ResortEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortUserEntity> resortUserEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortUser relationship helpers
    // -------------------------------------------------------------------------

    public void addResortUserEntity(ResortUserEntity entity) {
        addChild(resortUserEntities, entity, ResortUserEntity::assignResort, this);
    }

    public void removeResortUserEntity(ResortUserEntity entity) {
        removeChild(resortUserEntities, entity, (child, ignored) -> child.unassignResort());
    }

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "resortEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResortBasicInfoEntity resortBasicInfoEntity;

    // -------------------------------------------------------------------------
    // ResortBasicInfo relationship helpers (one-to-one — every resort has at
    // most one ResortBasicInfo)
    // -------------------------------------------------------------------------

    public void assignResortBasicInfoEntity(ResortBasicInfoEntity entity) {
        this.resortBasicInfoEntity = entity;
        entity.assignResort(this);
    }

    public void unassignResortBasicInfoEntity() {
        if (this.resortBasicInfoEntity != null) {
            this.resortBasicInfoEntity.assignResort(null);
        }
        this.resortBasicInfoEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "resortEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResortAddressEntity resortAddressEntity;

    // -------------------------------------------------------------------------
    // ResortAddress relationship helpers (one-to-one — every resort has at
    // most one ResortAddress)
    // -------------------------------------------------------------------------

    public void assignResortAddressEntity(ResortAddressEntity entity) {
        this.resortAddressEntity = entity;
        entity.assignResort(this);
    }

    public void unassignResortAddressEntity() {
        if (this.resortAddressEntity != null) {
            this.resortAddressEntity.assignResort(null);
        }
        this.resortAddressEntity = null;
    }

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortContactEntity> resortContactEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortContact relationship helpers
    // -------------------------------------------------------------------------

    public void addResortContactEntity(ResortContactEntity entity) {
        addChild(resortContactEntities, entity, ResortContactEntity::assignResort, this);
    }

    public void removeResortContactEntity(ResortContactEntity entity) {
        removeChild(resortContactEntities, entity, (child, ignored) -> child.unassignResort());
    }

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortFacilityEntity> resortFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityEntity(ResortFacilityEntity entity) {
        addChild(resortFacilityEntities, entity, ResortFacilityEntity::assignResort, this);
    }

    public void removeResortFacilityEntity(ResortFacilityEntity entity) {
        removeChild(resortFacilityEntities, entity, (child, ignored) -> child.unassignResort());
    }

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryEntity> resortRoomCategoryEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategory relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryEntity(ResortRoomCategoryEntity entity) {
        addChild(resortRoomCategoryEntities, entity, ResortRoomCategoryEntity::assignResort, this);
    }

    public void removeResortRoomCategoryEntity(ResortRoomCategoryEntity entity) {
        removeChild(resortRoomCategoryEntities, entity, (child, ignored) -> child.unassignResort());
    }

    @OneToMany(mappedBy = "resortEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortWeeklyScheduleDayEntity> resortWeeklyScheduleDayEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortWeeklyScheduleDay relationship helpers
    // -------------------------------------------------------------------------

    public void addResortWeeklyScheduleDayEntity(ResortWeeklyScheduleDayEntity entity) {
        addChild(resortWeeklyScheduleDayEntities, entity, ResortWeeklyScheduleDayEntity::assignResort, this);
    }

    public void removeResortWeeklyScheduleDayEntity(ResortWeeklyScheduleDayEntity entity) {
        removeChild(resortWeeklyScheduleDayEntities, entity, (child, ignored) -> child.unassignResort());
    }
}
