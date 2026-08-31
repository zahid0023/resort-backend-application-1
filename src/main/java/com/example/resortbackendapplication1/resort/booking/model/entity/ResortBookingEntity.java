package com.example.resortbackendapplication1.resort.booking.model.entity;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.bookingsource.model.entity.BookingSourceEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.entity.ResortRoomReservationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

/**
 * A customer booking one or more rooms in a single transaction (e.g. "2 Standard rooms, Sep 10-12"). Each room
 * is still its own independent {@link ResortRoomReservationEntity} (own status/price/cancellation) — this row exists
 * purely so those reservations can be queried/shown together. Every reservation belongs to exactly one
 * booking, even a lone single-room one (a "group of one") — {@code ResortRoomReservationEntity.resortBookingEntity} is never
 * null.
 */
@Getter
@Setter
@Entity
@Table(name = "resort_bookings")
public class ResortBookingEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity userEntity;

    /**
     * Human-readable code (e.g. "BK00000123") a customer can quote back over phone/WhatsApp instead of this
     * row's raw numeric id. Set once, in {@code ResortBookingServiceImpl#create}, from {@code resort_booking_reference_code_seq}
     * — never supplied by a request DTO, never updated afterward.
     */
    @Column(name = "reference_code", nullable = false, updatable = false)
    private String referenceCode;

    /**
     * Which channel this whole booking originated from (WHATSAPP/PHONE/WEBSITE/OTA/etc.) — owned exclusively
     * by the booking, not duplicated per room reservation; a room reservation resolves its channel by reaching
     * through {@code resortBookingEntity.getBookingSourceEntity()} rather than storing its own.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_source_id", nullable = false)
    private BookingSourceEntity bookingSourceEntity;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "notes", nullable = false, columnDefinition = "text")
    private String notes = "";

    /**
     * Owned entirely by this booking (cascade = ALL): {@code ResortRoomReservationService#create} only builds
     * and attaches these entities in memory, it never saves them itself — persisting this booking (see
     * {@code ResortBookingServiceImpl#createPosBooking}) is what cascades the insert of every reservation (and,
     * transitively, their own owned guests/nightly prices) in one go. Contrast with
     * {@code ResortRoomEntity#resortRoomReservationEntities}, which has no cascade at all: a reservation is a
     * historical record of a room, not owned by it, but it is owned by the booking it was placed under.
     */
    @OneToMany(mappedBy = "resortBookingEntity", cascade = CascadeType.ALL)
    private Set<ResortRoomReservationEntity> resortRoomReservationEntities = new LinkedHashSet<>();

    public void addResortRoomReservationEntity(ResortRoomReservationEntity entity) {
        addChild(resortRoomReservationEntities, entity, ResortRoomReservationEntity::assignResortBooking, this);
    }

    public void removeResortRoomReservationEntity(ResortRoomReservationEntity entity) {
        removeChild(resortRoomReservationEntities, entity, (child, ignored) -> child.unassignResortBooking());
    }
}
