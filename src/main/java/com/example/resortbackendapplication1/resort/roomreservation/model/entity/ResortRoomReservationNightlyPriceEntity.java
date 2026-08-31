package com.example.resortbackendapplication1.resort.roomreservation.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.pricing.PricingCalculator.RateType;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * One night's frozen price within a room reservation's stay — see V46.1. Owned entirely by its
 * ResortRoomReservationEntity (cascade = ALL, orphanRemoval = true on the parent side) — never created/deleted
 * through its own ServiceImpl, since it has no lifecycle independent of the reservation it belongs to.
 */
@Getter
@Setter
@Entity
@Table(name = "resort_room_reservation_nightly_prices")
public class ResortRoomReservationNightlyPriceEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resort_room_reservation_id", nullable = false)
    private ResortRoomReservationEntity resortRoomReservationEntity;

    /** Internal — call via {@link ResortRoomReservationEntity#addResortRoomReservationNightlyPriceEntity}. */
    public void assignResortRoomReservation(ResortRoomReservationEntity resortRoomReservationEntity) {
        this.resortRoomReservationEntity = resortRoomReservationEntity;
    }

    /** Internal — call via {@link ResortRoomReservationEntity#removeResortRoomReservationNightlyPriceEntity}. */
    public void unassignResortRoomReservation() {
        this.resortRoomReservationEntity = null;
    }

    @NotNull
    @Column(name = "night_date", nullable = false)
    private LocalDate nightDate;

    /** Which rule won this night — see PricingCalculator. Maps to the Postgres enum {@code rate_type}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "rate_type", nullable = false, columnDefinition = "rate_type")
    private RateType rateType;

    @NotNull
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
