package com.example.resortbackendapplication1.roomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortRoomCategoryEntity;
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
@Table(name = "room_categories")
public class RoomCategoryEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "roomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomCategoryLocaleEntity> roomCategoryLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // RoomCategory Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addRoomCategoryLocaleEntity(RoomCategoryLocaleEntity entity) {
        addChild(roomCategoryLocaleEntities, entity, RoomCategoryLocaleEntity::assignRoomCategory, this);
    }

    public void removeRoomCategoryLocaleEntity(RoomCategoryLocaleEntity entity) {
        removeChild(roomCategoryLocaleEntities, entity, (child, ignored) -> child.unassignRoomCategory());
    }

    @OneToMany(mappedBy = "roomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryEntity> resortRoomCategoryEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortRoomCategory relationship helpers
    // -------------------------------------------------------------------------

    public void addResortRoomCategoryEntity(ResortRoomCategoryEntity entity) {
        addChild(resortRoomCategoryEntities, entity, ResortRoomCategoryEntity::assignRoomCategory, this);
    }

    public void removeResortRoomCategoryEntity(ResortRoomCategoryEntity entity) {
        removeChild(resortRoomCategoryEntities, entity, (child, ignored) -> child.unassignRoomCategory());
    }
}
