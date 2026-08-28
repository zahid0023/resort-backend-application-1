package com.example.resortbackendapplication1.resort.room.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resort_room_facilities")
public class ResortRoomFacilityEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addResortRoomFacilityEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeResortRoomFacilityEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_facility_group_id", nullable = false)
    private ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity;

    /** Internal — call via {@link ResortRoomFacilityGroupEntity#addResortRoomFacilityEntity}. */
    public void assignResortRoomFacilityGroup(ResortRoomFacilityGroupEntity resortRoomFacilityGroupEntity) {
        this.resortRoomFacilityGroupEntity = resortRoomFacilityGroupEntity;
    }

    /** Internal — call via {@link ResortRoomFacilityGroupEntity#removeResortRoomFacilityEntity}. */
    public void unassignResortRoomFacilityGroup() {
        this.resortRoomFacilityGroupEntity = null;
    }

    /** Optional link to a platform-defined facility. Null means this is a resort-room-defined custom facility. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private FacilityEntity facilityEntity;

    /** Resort-room-scoped identifier, unique per resort room. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_highlighted", nullable = false)
    private Boolean isHighlighted = false;

    @Size(max = 100)
    @Column(name = "icon_type", length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "resortRoomFacilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomFacilityLocaleEntity> resortRoomFacilityLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomFacilityLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityLocaleEntity(ResortRoomFacilityLocaleEntity entity) {
        addChild(resortRoomFacilityLocaleEntities, entity, ResortRoomFacilityLocaleEntity::assignResortRoomFacility, this);
    }

    public void removeResortRoomFacilityLocaleEntity(ResortRoomFacilityLocaleEntity entity) {
        removeChild(resortRoomFacilityLocaleEntities, entity, (child, ignored) -> child.unassignResortRoomFacility());
    }
}
