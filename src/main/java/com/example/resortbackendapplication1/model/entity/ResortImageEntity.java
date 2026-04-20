package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.ImageEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "resort_images")
public class ResortImageEntity extends ImageEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @Column(name = "image_url", nullable = false, length = Integer.MAX_VALUE)
    private String imageUrl;

    @Size(max = 255)
    @Column(name = "public_id")
    private String publicId;

    @Size(max = 255)
    @Column(name = "caption")
    private String caption;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}