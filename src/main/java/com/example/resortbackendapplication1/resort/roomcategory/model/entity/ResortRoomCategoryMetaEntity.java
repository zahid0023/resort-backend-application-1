package com.example.resortbackendapplication1.resort.roomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_metas")
public class ResortRoomCategoryMetaEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    /** Internal — call via {@link ResortRoomCategoryEntity#assignResortRoomCategoryMetaEntity}. */
    public void assignResortRoomCategory(ResortRoomCategoryEntity resortRoomCategoryEntity) {
        this.resortRoomCategoryEntity = resortRoomCategoryEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryEntity#unassignResortRoomCategoryMetaEntity}. */
    public void unassignResortRoomCategory() {
        this.resortRoomCategoryEntity = null;
    }

    @NotNull
    @ColumnDefault("2")
    @Column(name = "max_adults", nullable = false)
    private Integer maxAdults = 2;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "max_children", nullable = false)
    private Integer maxChildren = 0;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "max_infants", nullable = false)
    private Integer maxInfants = 0;

    @NotNull
    @ColumnDefault("2")
    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy = 2;

    @Column(name = "room_size", precision = 10, scale = 2)
    private BigDecimal roomSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_size_unit_id")
    private UnitEntity roomSizeUnitEntity;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "bedroom_count", nullable = false)
    private Integer bedroomCount = 1;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "bathroom_count", nullable = false)
    private Integer bathroomCount = 1;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "minimum_stay_nights", nullable = false)
    private Integer minimumStayNights = 1;

    @Column(name = "maximum_stay_nights")
    private Integer maximumStayNights;
}
