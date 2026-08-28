package com.example.resortbackendapplication1.roomstatus.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "room_statuses")
public class RoomStatusEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "roomStatusEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomStatusLocaleEntity> roomStatusLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // RoomStatus Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addRoomStatusLocaleEntity(RoomStatusLocaleEntity entity) {
        addChild(roomStatusLocaleEntities, entity, RoomStatusLocaleEntity::assignRoomStatus, this);
    }

    public void removeRoomStatusLocaleEntity(RoomStatusLocaleEntity entity) {
        removeChild(roomStatusLocaleEntities, entity, (child, ignored) -> child.unassignRoomStatus());
    }

    @OneToMany(mappedBy = "roomStatusEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomEntity> resortRoomEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoom relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomEntity(ResortRoomEntity entity) {
        addChild(resortRoomEntities, entity, ResortRoomEntity::assignRoomStatus, this);
    }

    public void removeResortRoomEntity(ResortRoomEntity entity) {
        removeChild(resortRoomEntities, entity, (child, ignored) -> child.unassignRoomStatus());
    }
}
