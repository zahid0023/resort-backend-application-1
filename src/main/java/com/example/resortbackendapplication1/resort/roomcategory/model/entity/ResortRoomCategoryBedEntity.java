package com.example.resortbackendapplication1.resort.roomcategory.model.entity;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_beds")
public class ResortRoomCategoryBedEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#addResortRoomCategoryBedEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#removeResortRoomCategoryBedEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bed_type_id", nullable = false)
    private BedTypeEntity bedTypeEntity;

    /** Internal — call via {@link BedTypeEntity#addResortRoomCategoryBedEntity}. */
    public void assignBedType(BedTypeEntity bedTypeEntity) {
        this.bedTypeEntity = bedTypeEntity;
    }

    /** Internal — call via {@link BedTypeEntity#removeResortRoomCategoryBedEntity}. */
    public void unassignBedType() {
        this.bedTypeEntity = null;
    }

    @NotNull
    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_extra_bed_allowed", nullable = false)
    private Boolean isExtraBedAllowed = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "max_extra_beds", nullable = false)
    private Integer maxExtraBeds = 0;
}
