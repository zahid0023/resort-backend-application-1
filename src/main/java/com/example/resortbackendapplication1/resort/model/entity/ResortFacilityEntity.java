package com.example.resortbackendapplication1.resort.model.entity;

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
@Table(name = "resort_facilities")
public class ResortFacilityEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortFacilityEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortFacilityEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_facility_group_id", nullable = false)
    private ResortFacilityGroupEntity resortFacilityGroupEntity;

    /** Internal — call via {@link ResortFacilityGroupEntity#addResortFacilityEntity}. */
    public void assignResortFacilityGroup(ResortFacilityGroupEntity resortFacilityGroupEntity) {
        this.resortFacilityGroupEntity = resortFacilityGroupEntity;
    }

    /** Internal — call via {@link ResortFacilityGroupEntity#removeResortFacilityEntity}. */
    public void unassignResortFacilityGroup() {
        this.resortFacilityGroupEntity = null;
    }

    /** Optional link to a platform-defined facility. Null means this is a resort-defined custom facility. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private FacilityEntity facilityEntity;

    /** Resort-scoped identifier, unique per resort. Immutable after creation. */
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

    @OneToMany(mappedBy = "resortFacilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortFacilityLocaleEntity> resortFacilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resortFacilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortFacilityOperatingHoursEntity> resortFacilityOperatingHoursEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortFacilityLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityLocaleEntity(ResortFacilityLocaleEntity entity) {
        addChild(resortFacilityLocaleEntities, entity, ResortFacilityLocaleEntity::assignResortFacility, this);
    }

    public void removeResortFacilityLocaleEntity(ResortFacilityLocaleEntity entity) {
        removeChild(resortFacilityLocaleEntities, entity, (child, ignored) -> child.unassignResortFacility());
    }

    // -------------------------------------------------------------------------
    // ResortFacilityOperatingHours relationship helpers
    // -------------------------------------------------------------------------

    public void addResortFacilityOperatingHoursEntity(ResortFacilityOperatingHoursEntity entity) {
        addChild(resortFacilityOperatingHoursEntities, entity, ResortFacilityOperatingHoursEntity::assignResortFacility, this);
    }

    public void removeResortFacilityOperatingHoursEntity(ResortFacilityOperatingHoursEntity entity) {
        removeChild(resortFacilityOperatingHoursEntities, entity, (child, ignored) -> child.unassignResortFacility());
    }
}
