package com.example.resortbackendapplication1.resort.roomreservation.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.booking.model.entity.ResortBookingEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

/**
 * The central, channel-independent booking object — see V46__create_resort_room_reservations_table.sql.
 * blocksAvailability is recalculated by the fn_sync_resort_room_reservation_blocks_availability DB trigger on
 * every insert and on every update of reservationStatusEntity; the application never sets it directly.
 */
@Getter
@Setter
@Entity
@Table(name = "resort_room_reservations")
public class ResortRoomReservationEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addResortRoomReservationEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeResortRoomReservationEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_status_id", nullable = false)
    private ReservationStatusEntity reservationStatusEntity;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    /**
     * Every occupant of this room, by name — distinct from the booking's own customer (the booker/payer).
     * Owned entirely by this reservation (cascade = ALL, orphanRemoval = true): a guest row is created/deleted
     * only as part of creating/deleting the reservation it belongs to, never independently.
     */
    @OneToMany(mappedBy = "resortRoomReservationEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomReservationGuestEntity> resortRoomReservationGuestEntities = new LinkedHashSet<>();

    public void addResortRoomReservationGuestEntity(ResortRoomReservationGuestEntity entity) {
        addChild(resortRoomReservationGuestEntities, entity, ResortRoomReservationGuestEntity::assignResortRoomReservation, this);
    }

    public void removeResortRoomReservationGuestEntity(ResortRoomReservationGuestEntity entity) {
        removeChild(resortRoomReservationGuestEntities, entity, (child, ignored) -> child.unassignResortRoomReservation());
    }

    /**
     * One row per night of the stay, frozen at booking time — see V46.1. Owned entirely by this reservation
     * (cascade = ALL, orphanRemoval = true), the same way {@link #resortRoomReservationGuestEntities} is.
     */
    @OneToMany(mappedBy = "resortRoomReservationEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomReservationNightlyPriceEntity> resortRoomReservationNightlyPriceEntities = new LinkedHashSet<>();

    public void addResortRoomReservationNightlyPriceEntity(ResortRoomReservationNightlyPriceEntity entity) {
        addChild(resortRoomReservationNightlyPriceEntities, entity, ResortRoomReservationNightlyPriceEntity::assignResortRoomReservation, this);
    }

    public void removeResortRoomReservationNightlyPriceEntity(ResortRoomReservationNightlyPriceEntity entity) {
        removeChild(resortRoomReservationNightlyPriceEntities, entity, (child, ignored) -> child.unassignResortRoomReservation());
    }

    /**
     * Every reservation belongs to exactly one booking, even a lone single-room booking (a "group of
     * one") — see {@link ResortBookingEntity}'s own javadoc. There is no customer field on this entity: the customer
     * is owned entirely by the booking, reached via {@code resortBookingEntity.getUserEntity()}.
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_booking_id", nullable = false)
    private ResortBookingEntity resortBookingEntity;

    /** Internal — call via {@link ResortBookingEntity#addResortRoomReservationEntity}. */
    public void assignResortBooking(ResortBookingEntity resortBookingEntity) {
        this.resortBookingEntity = resortBookingEntity;
    }

    /** Internal — call via {@link ResortBookingEntity#removeResortRoomReservationEntity}. */
    public void unassignResortBooking() {
        this.resortBookingEntity = null;
    }

    /**
     * The row this one supersedes, if any — null on a reservation's first row. See
     * {@link #blocksAvailability}'s note and the PUT .../reservations/{id}/status flow: status changes never
     * mutate a row in place, they soft-delete this row's predecessor and insert a new row pointing back here.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_resort_room_reservation_id")
    private ResortRoomReservationEntity previousResortRoomReservationEntity;

    @NotNull
    @Min(1)
    @ColumnDefault("1")
    @Column(name = "adult_count", nullable = false)
    private Integer adultCount = 1;

    @NotNull
    @Min(0)
    @ColumnDefault("0")
    @Column(name = "child_count", nullable = false)
    private Integer childCount = 0;

    /** Currency of {@link #totalPrice}, frozen at booking time. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    /** Billing unit of {@link #totalPrice}, frozen at booking time. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_unit_id", nullable = false)
    private PriceUnitEntity priceUnitEntity;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "notes", nullable = false, columnDefinition = "text")
    private String notes = "";

    /**
     * Free-text reason captured on the transition that produced this row's status (e.g. why it's CANCELLED or
     * NO_SHOW). Null on every row whose status was never explained. Not carried forward from the row this one
     * supersedes; each row's reason describes only its own transition.
     */
    @Column(name = "cancellation_reason", columnDefinition = "text")
    private String cancellationReason;

    /**
     * True for statuses that occupy the room (PENDING/CONFIRMED/CHECKED_IN); false for statuses that free it
     * (CANCELLED/NO_SHOW/CHECKED_OUT). Owned exclusively by the DB trigger — never set from application code.
     */
    @NotNull
    @ColumnDefault("true")
    @Column(name = "blocks_availability", nullable = false)
    private Boolean blocksAvailability = true;
}
