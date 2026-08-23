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
@Table(name = "resort_room_category_facilities")
public class ResortRoomCategoryFacilityEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategoryFacilityEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategoryFacilityEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_facility_group_id", nullable = false)
    private ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity;

    /** Internal — call via {@link ResortRoomCategoryFacilityGroupEntity#addResortRoomCategoryFacilityEntity}. */
    public void assignResortRoomCategoryFacilityGroup(ResortRoomCategoryFacilityGroupEntity resortRoomCategoryFacilityGroupEntity) {
        this.resortRoomCategoryFacilityGroupEntity = resortRoomCategoryFacilityGroupEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryFacilityGroupEntity#removeResortRoomCategoryFacilityEntity}. */
    public void unassignResortRoomCategoryFacilityGroup() {
        this.resortRoomCategoryFacilityGroupEntity = null;
    }

    /** Optional link to a platform-defined facility. Null means this is a resort-room-category-defined custom facility. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private FacilityEntity facilityEntity;

    /** Resort-room-category-scoped identifier, unique per resort room category. Immutable after creation. */
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

    @OneToMany(mappedBy = "resortRoomCategoryFacilityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryFacilityLocaleEntity> resortRoomCategoryFacilityLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategoryFacilityLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryFacilityLocaleEntity(ResortRoomCategoryFacilityLocaleEntity entity) {
        addChild(resortRoomCategoryFacilityLocaleEntities, entity, ResortRoomCategoryFacilityLocaleEntity::assignResortRoomCategoryFacility, this);
    }

    public void removeResortRoomCategoryFacilityLocaleEntity(ResortRoomCategoryFacilityLocaleEntity entity) {
        removeChild(resortRoomCategoryFacilityLocaleEntities, entity, (child, ignored) -> child.unassignResortRoomCategoryFacility());
    }
}
