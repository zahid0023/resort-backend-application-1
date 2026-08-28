package com.example.resortbackendapplication1.resort.core.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import com.example.resortbackendapplication1.resort.core.model.enums.DayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    /** WEEKDAY/WEEKEND only. Immutable after creation. Maps to the Postgres native enum {@code day_type}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "day_type", nullable = false, columnDefinition = "day_type")
    private DayType dayType;

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
