package com.example.resortbackendapplication1.resortroomcategory.model.entity;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_room_category_beds")
public class ResortRoomCategoryBedEntity extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_room_category_id", nullable = false)
    private ResortRoomCategoryEntity resortRoomCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bed_type_id", nullable = false)
    private BedTypeEntity bedTypeEntity;

    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
}
