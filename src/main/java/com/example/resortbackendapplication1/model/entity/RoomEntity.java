package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class RoomEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("''")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 50)
    @Column(name = "room_number", length = 50)
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @NotNull
    @Column(name = "max_adults", nullable = false)
    private Integer maxAdults;

    @ColumnDefault("0")
    @Column(name = "max_children")
    private Integer maxChildren;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "max_occupancy", insertable = false, updatable = false)
    private Integer maxOccupancy;

    @NotNull
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;
}