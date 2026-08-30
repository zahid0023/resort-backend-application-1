package com.example.resortbackendapplication1.resort.reservation.model.entity;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.currency.model.entity.CurrencyEntity;
import com.example.resortbackendapplication1.price.model.entity.PriceUnitEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationSourceEntity;
import com.example.resortbackendapplication1.reservation.model.entity.ReservationStatusEntity;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The central, channel-independent booking object — see V45__create_reservations_table.sql. blocksAvailability
 * is recalculated by the fn_sync_reservation_blocks_availability DB trigger on every insert and on every update
 * of reservationStatusEntity; the application never sets it directly.
 */
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class ReservationEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity userEntity;

    /** Internal — call via {@link UserEntity#addReservationEntity}. */
    public void assignUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    /** Internal — call via {@link UserEntity#removeReservationEntity}. */
    public void unassignUser() {
        this.userEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addReservationEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeReservationEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_status_id", nullable = false)
    private ReservationStatusEntity reservationStatusEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_source_id", nullable = false)
    private ReservationSourceEntity reservationSourceEntity;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    /**
     * Every reservation belongs to exactly one booking group, even a lone single-room booking (a "group of
     * one") — see {@link BookingGroupEntity}'s own javadoc.
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_group_id", nullable = false)
    private BookingGroupEntity bookingGroupEntity;

    /** Internal — call via {@link BookingGroupEntity#addReservationEntity}. */
    public void assignBookingGroup(BookingGroupEntity bookingGroupEntity) {
        this.bookingGroupEntity = bookingGroupEntity;
    }

    /** Internal — call via {@link BookingGroupEntity#removeReservationEntity}. */
    public void unassignBookingGroup() {
        this.bookingGroupEntity = null;
    }

    /**
     * The row this one supersedes, if any — null on a reservation's first row. See
     * {@link #blocksAvailability}'s note and the PUT .../reservations/{id}/status flow: status changes never
     * mutate a row in place, they soft-delete this row's predecessor and insert a new row pointing back here.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_reservation_id")
    private ReservationEntity previousReservationEntity;

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
     * True for statuses that occupy the room (PENDING/CONFIRMED/CHECKED_IN); false for statuses that free it
     * (CANCELLED/NO_SHOW/CHECKED_OUT). Owned exclusively by the DB trigger — never set from application code.
     */
    @NotNull
    @ColumnDefault("true")
    @Column(name = "blocks_availability", nullable = false)
    private Boolean blocksAvailability = true;
}
