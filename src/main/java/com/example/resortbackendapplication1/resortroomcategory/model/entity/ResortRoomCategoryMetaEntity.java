package com.example.resortbackendapplication1.resortroomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.unit.model.entity.UnitEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_metas")
public class ResortRoomCategoryMetaEntity extends AuditableEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    @ColumnDefault("2")
    @Column(name = "max_adults", nullable = false)
    private Integer maxAdults = 2;

    @ColumnDefault("0")
    @Column(name = "max_children", nullable = false)
    private Integer maxChildren = 0;

    @ColumnDefault("0")
    @Column(name = "max_infants", nullable = false)
    private Integer maxInfants = 0;

    @ColumnDefault("2")
    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy = 2;

    @Column(name = "room_size", precision = 10, scale = 2)
    private BigDecimal roomSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_size_unit_id")
    private UnitEntity roomSizeUnit;

    @ColumnDefault("1")
    @Column(name = "bedroom_count", nullable = false)
    private Integer bedroomCount = 1;

    @ColumnDefault("1")
    @Column(name = "bathroom_count", nullable = false)
    private Integer bathroomCount = 1;

    @Column(name = "default_check_in_time")
    private LocalTime defaultCheckInTime;

    @Column(name = "default_check_out_time")
    private LocalTime defaultCheckOutTime;

    @ColumnDefault("false")
    @Column(name = "is_extra_bed_allowed", nullable = false)
    private Boolean isExtraBedAllowed = false;

    @ColumnDefault("0")
    @Column(name = "max_extra_beds", nullable = false)
    private Integer maxExtraBeds = 0;

    @ColumnDefault("false")
    @Column(name = "is_smoking_allowed", nullable = false)
    private Boolean isSmokingAllowed = false;

    @ColumnDefault("false")
    @Column(name = "is_pets_allowed", nullable = false)
    private Boolean isPetsAllowed = false;

    @ColumnDefault("1")
    @Column(name = "minimum_stay_nights", nullable = false)
    private Integer minimumStayNights = 1;

    @Column(name = "maximum_stay_nights")
    private Integer maximumStayNights;
}
