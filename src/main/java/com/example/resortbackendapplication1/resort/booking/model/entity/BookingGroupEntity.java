package com.example.resortbackendapplication1.resort.booking.model.entity;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.reservation.model.entity.ReservationEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

/**
 * A customer booking one or more rooms in a single transaction (e.g. "2 Standard rooms, Sep 10-12"). Each room
 * is still its own independent {@link ReservationEntity} (own status/price/cancellation) — this row exists
 * purely so those reservations can be queried/shown together. Not every reservation belongs to a group; a
 * lone single-room booking has none (ReservationEntity.bookingGroupEntity is nullable).
 */
@Getter
@Setter
@Entity
@Table(name = "booking_groups")
public class BookingGroupEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "bookingGroupEntity")
    private Set<ReservationEntity> reservationEntities = new LinkedHashSet<>();

    public void addReservationEntity(ReservationEntity entity) {
        addChild(reservationEntities, entity, ReservationEntity::assignBookingGroup, this);
    }

    public void removeReservationEntity(ReservationEntity entity) {
        removeChild(reservationEntities, entity, (child, ignored) -> child.unassignBookingGroup());
    }
}
