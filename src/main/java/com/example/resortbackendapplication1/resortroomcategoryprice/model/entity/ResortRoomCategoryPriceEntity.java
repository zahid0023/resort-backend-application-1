package com.example.resortbackendapplication1.resortroomcategoryprice.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceTypeEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.resortroomcategory.model.entity.ResortRoomCategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_prices")
public class ResortRoomCategoryPriceEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_type_id", nullable = false)
    private PriceTypeEntity priceTypeEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_unit_id", nullable = false)
    private PriceUnitEntity priceUnitEntity;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "monday", nullable = false)
    private Boolean monday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "tuesday", nullable = false)
    private Boolean tuesday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "wednesday", nullable = false)
    private Boolean wednesday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "thursday", nullable = false)
    private Boolean thursday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "friday", nullable = false)
    private Boolean friday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "saturday", nullable = false)
    private Boolean saturday = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "sunday", nullable = false)
    private Boolean sunday = false;
}
