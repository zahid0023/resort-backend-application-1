package com.example.resortbackendapplication1.facility.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "facility_groups")
public class FacilityGroupEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @NotBlank
    @Size(max = 100)
    @Column(name = "icon_type", nullable = false, length = 100)
    private String iconType;

    @Column(name = "icon_value", columnDefinition = "text")
    private String iconValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "icon_meta", columnDefinition = "jsonb")
    private Map<String, Object> iconMeta;

    @OneToMany(mappedBy = "facilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityGroupLocaleEntity> facilityGroupLocaleEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityGroupScopeAssignmentEntity> facilityGroupScopeAssignmentEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facilityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacilityEntity> facilityEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // FacilityGroup Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityGroupLocaleEntity(FacilityGroupLocaleEntity entity) {
        addChild(facilityGroupLocaleEntities, entity, FacilityGroupLocaleEntity::assignFacilityGroup, this);
    }

    public void removeFacilityGroupLocaleEntity(FacilityGroupLocaleEntity entity) {
        removeChild(facilityGroupLocaleEntities, entity, (child, ignored) -> child.unassignFacilityGroup());
    }

    // -------------------------------------------------------------------------
    // FacilityGroupScopeAssignment relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityGroupScopeAssignmentEntity(FacilityGroupScopeAssignmentEntity entity) {
        addChild(facilityGroupScopeAssignmentEntities, entity, FacilityGroupScopeAssignmentEntity::assignFacilityGroup, this);
    }

    public void removeFacilityGroupScopeAssignmentEntity(FacilityGroupScopeAssignmentEntity entity) {
        removeChild(facilityGroupScopeAssignmentEntities, entity, (child, ignored) -> child.unassignFacilityGroup());
    }

    // -------------------------------------------------------------------------
    // Facility relationship helpers
    // -------------------------------------------------------------------------

    public void addFacilityEntity(FacilityEntity entity) {
        addChild(facilityEntities, entity, FacilityEntity::assignFacilityGroup, this);
    }

    public void removeFacilityEntity(FacilityEntity entity) {
        removeChild(facilityEntities, entity, (child, ignored) -> child.unassignFacilityGroup());
    }
}
