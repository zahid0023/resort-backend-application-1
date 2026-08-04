package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "facilities")
public class FacilityEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "facility_group_id", nullable = false)
    private FacilityGroupEntity facilityGroupEntity;

    /** Internal — call via {@link FacilityGroupEntity#addFacilityEntity}. */
    public void assignFacilityGroup(FacilityGroupEntity facilityGroupEntity) {
        this.facilityGroupEntity = facilityGroupEntity;
    }

    /** Internal — call via {@link FacilityGroupEntity#removeFacilityEntity}. */
    public void unassignFacilityGroup() {
        this.facilityGroupEntity = null;
    }

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotBlank
    @Size(max = 100)
    @Column(name = "icon_type", nullable = false, length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "facilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityLocaleEntity> facilityLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityScopeAssignmentEntity> facilityScopeAssignmentEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // Facility Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityLocaleEntity(FacilityLocaleEntity entity) {
        addChild(facilityLocaleEntities, entity, FacilityLocaleEntity::assignFacility, this);
    }

    public void removeFacilityLocaleEntity(FacilityLocaleEntity entity) {
        removeChild(facilityLocaleEntities, entity, (child, ignored) -> child.unassignFacility());
    }

    // -------------------------------------------------------------------------
    // FacilityScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        addChild(facilityScopeAssignmentEntities, entity, FacilityScopeAssignmentEntity::assignFacility, this);
    }

    public void removeFacilityScopeAssignmentEntity(FacilityScopeAssignmentEntity entity) {
        removeChild(facilityScopeAssignmentEntities, entity, (child, ignored) -> child.unassignFacility());
    }
}
