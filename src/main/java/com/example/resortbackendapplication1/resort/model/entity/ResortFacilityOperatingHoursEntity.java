package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "resort_facility_operating_hours")
public class ResortFacilityOperatingHoursEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_facility_id", nullable = false)
    private ResortFacilityEntity resortFacilityEntity;

    /** Internal — call via {@link ResortFacilityEntity#addResortFacilityOperatingHoursEntity}. */
    public void assignResortFacility(ResortFacilityEntity resortFacilityEntity) {
        this.resortFacilityEntity = resortFacilityEntity;
    }

    /** Internal — call via {@link ResortFacilityEntity#removeResortFacilityOperatingHoursEntity}. */
    public void unassignResortFacility() {
        this.resortFacilityEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeekEntity dayOfWeekEntity;

    /** Internal — call via {@link DayOfWeekEntity#addResortFacilityOperatingHoursEntity}. */
    public void assignDayOfWeek(DayOfWeekEntity dayOfWeekEntity) {
        this.dayOfWeekEntity = dayOfWeekEntity;
    }

    /** Internal — call via {@link DayOfWeekEntity#removeResortFacilityOperatingHoursEntity}. */
    public void unassignDayOfWeek() {
        this.dayOfWeekEntity = null;
    }

    @Column(name = "opens_at")
    private LocalTime opensAt;

    @Column(name = "closes_at")
    private LocalTime closesAt;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_twenty_four_hours", nullable = false)
    private Boolean isTwentyFourHours = false;
}
