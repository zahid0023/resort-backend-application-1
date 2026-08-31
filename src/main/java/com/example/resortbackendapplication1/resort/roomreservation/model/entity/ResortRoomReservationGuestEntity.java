package com.example.resortbackendapplication1.resort.roomreservation.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.roomreservation.model.enums.GuestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

/**
 * One occupant of a room — a reservation's adult_count/child_count track headcount only, not identity, so a
 * room with more than one person staying in it needs one row per person here. Owned entirely by its
 * ResortRoomReservationEntity (cascade = ALL, orphanRemoval = true on the parent side) — never created/deleted through
 * its own ServiceImpl, since it has no lifecycle independent of the reservation it belongs to.
 */
@Getter
@Setter
@Entity
@Table(name = "resort_room_reservation_guests")
public class ResortRoomReservationGuestEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resort_room_reservation_id", nullable = false)
    private ResortRoomReservationEntity resortRoomReservationEntity;

    /** Internal — call via {@link ResortRoomReservationEntity#addResortRoomReservationGuestEntity}. */
    public void assignResortRoomReservation(ResortRoomReservationEntity resortRoomReservationEntity) {
        this.resortRoomReservationEntity = resortRoomReservationEntity;
    }

    /** Internal — call via {@link ResortRoomReservationEntity#removeResortRoomReservationGuestEntity}. */
    public void unassignResortRoomReservation() {
        this.resortRoomReservationEntity = null;
    }

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    /** Reconciles with the parent reservation's adult_count/child_count. Maps to the Postgres enum {@code guest_type}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'ADULT'")
    @Column(name = "guest_type", nullable = false, columnDefinition = "guest_type")
    private GuestType guestType = GuestType.ADULT;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
