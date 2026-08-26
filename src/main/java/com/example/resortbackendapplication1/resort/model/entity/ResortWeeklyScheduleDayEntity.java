package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resort_weekly_schedule_days")
public class ResortWeeklyScheduleDayEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortWeeklyScheduleDayEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortWeeklyScheduleDayEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    /** WEEKDAY/WEEKEND only. Immutable after creation — enforced by a DB trigger. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_type_id", nullable = false)
    private PriceTypeEntity priceTypeEntity;

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeekEntity dayOfWeekEntity;

    /** Internal — call via {@link DayOfWeekEntity#addResortWeeklyScheduleDayEntity}. */
    public void assignDayOfWeek(DayOfWeekEntity dayOfWeekEntity) {
        this.dayOfWeekEntity = dayOfWeekEntity;
    }

    /** Internal — call via {@link DayOfWeekEntity#removeResortWeeklyScheduleDayEntity}. */
    public void unassignDayOfWeek() {
        this.dayOfWeekEntity = null;
    }
}
