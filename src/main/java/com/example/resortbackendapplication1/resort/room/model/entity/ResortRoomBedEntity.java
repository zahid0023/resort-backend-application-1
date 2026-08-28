package com.example.resortbackendapplication1.resort.room.model.entity;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_room_beds")
public class ResortRoomBedEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_id", nullable = false)
    private ResortRoomEntity resortRoomEntity;

    /** Internal — call via {@link ResortRoomEntity#addResortRoomBedEntity}. */
    public void assignResortRoom(ResortRoomEntity resortRoomEntity) {
        this.resortRoomEntity = resortRoomEntity;
    }

    /** Internal — call via {@link ResortRoomEntity#removeResortRoomBedEntity}. */
    public void unassignResortRoom() {
        this.resortRoomEntity = null;
    }

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bed_type_id", nullable = false)
    private BedTypeEntity bedTypeEntity;

    /** Internal — call via {@link BedTypeEntity#addResortRoomBedEntity}. */
    public void assignBedType(BedTypeEntity bedTypeEntity) {
        this.bedTypeEntity = bedTypeEntity;
    }

    /** Internal — call via {@link BedTypeEntity#removeResortRoomBedEntity}. */
    public void unassignBedType() {
        this.bedTypeEntity = null;
    }

    @NotNull
    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_extra_bed_allowed", nullable = false)
    private Boolean isExtraBedAllowed = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "max_extra_beds", nullable = false)
    private Integer maxExtraBeds = 0;
}
