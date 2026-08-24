package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
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
@Table(name = "resort_room_category_price_days")
public class ResortRoomCategoryPriceDayEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_price_id", nullable = false)
    private ResortRoomCategoryPriceEntity resortRoomCategoryPriceEntity;

    /** Internal — call via {@link ResortRoomCategoryPriceEntity#addResortRoomCategoryPriceDayEntity}. */
    public void assignResortRoomCategoryPrice(ResortRoomCategoryPriceEntity resortRoomCategoryPriceEntity) {
        this.resortRoomCategoryPriceEntity = resortRoomCategoryPriceEntity;
    }

    /** Internal — call via {@link ResortRoomCategoryPriceEntity#removeResortRoomCategoryPriceDayEntity}. */
    public void unassignResortRoomCategoryPrice() {
        this.resortRoomCategoryPriceEntity = null;
    }

    /** Only allowed on WEEKDAY/WEEKEND prices — enforced by a DB trigger. */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeekEntity dayOfWeekEntity;

    /** Internal — call via {@link DayOfWeekEntity#addResortRoomCategoryPriceDayEntity}. */
    public void assignDayOfWeek(DayOfWeekEntity dayOfWeekEntity) {
        this.dayOfWeekEntity = dayOfWeekEntity;
    }

    /** Internal — call via {@link DayOfWeekEntity#removeResortRoomCategoryPriceDayEntity}. */
    public void unassignDayOfWeek() {
        this.dayOfWeekEntity = null;
    }
}
