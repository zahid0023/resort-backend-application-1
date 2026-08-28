package com.example.resortbackendapplication1.resort.room.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.facility.model.entity.FacilityGroupEntity;
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
@Table(name = "resort_room_facility_groups")
public class ResortRoomFacilityGroupEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addResortRoomFacilityGroupEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeResortRoomFacilityGroupEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    /** Optional link to a platform-defined facility group. Null means a resort-defined custom group. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_group_id")
    private FacilityGroupEntity facilityGroupEntity;

    /** Resort-room-scoped identifier, unique per resort room. Immutable after creation. */
    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @Size(max = 100)
    @Column(name = "icon_type", length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "resortRoomFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomFacilityGroupLocaleEntity> resortRoomFacilityGroupLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomFacilityGroupLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityGroupLocaleEntity(ResortRoomFacilityGroupLocaleEntity entity) {
        addChild(resortRoomFacilityGroupLocaleEntities, entity, ResortRoomFacilityGroupLocaleEntity::assignResortRoomFacilityGroup, this);
    }

    public void removeResortRoomFacilityGroupLocaleEntity(ResortRoomFacilityGroupLocaleEntity entity) {
        removeChild(resortRoomFacilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignResortRoomFacilityGroup());
    }

    @OneToMany(mappedBy = "resortRoomFacilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomFacilityEntity> resortRoomFacilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomFacility relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomFacilityEntity(ResortRoomFacilityEntity entity) {
        addChild(resortRoomFacilityEntities, entity, ResortRoomFacilityEntity::assignResortRoomFacilityGroup, this);
    }

    public void removeResortRoomFacilityEntity(ResortRoomFacilityEntity entity) {
        removeChild(resortRoomFacilityEntities, entity, (child, ignored) -> child.unassignResortRoomFacilityGroup());
    }
}
