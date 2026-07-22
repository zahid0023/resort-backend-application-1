package com.example.resortbackendapplication1.resortroomcategoryprice.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.dayofweek.model.entity.DayOfWeekEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_price_days")
public class ResortRoomCategoryPriceDayEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_price_id", nullable = false)
    private ResortRoomCategoryPriceEntity resortRoomCategoryPriceEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeekEntity dayOfWeekEntity;
}
