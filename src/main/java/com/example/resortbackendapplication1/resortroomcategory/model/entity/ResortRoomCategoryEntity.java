package com.example.resortbackendapplication1.resortroomcategory.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import com.example.resortbackendapplication1.roomcategory.model.entity.RoomCategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resort_room_categories")
public class ResortRoomCategoryEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_category_id", nullable = false)
    private RoomCategoryEntity roomCategoryEntity;

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToOne(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ResortRoomCategoryMetaEntity meta;

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortRoomCategoryBedEntity> beds = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resortRoomCategoryEntity", cascade = CascadeType.ALL)
    private Set<ResortRoomCategoryLocaleEntity> resortRoomCategoryLocaleEntities = new LinkedHashSet<>();
}
