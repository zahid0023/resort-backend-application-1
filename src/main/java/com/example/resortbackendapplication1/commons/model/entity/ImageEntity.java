package com.example.resortbackendapplication1.commons.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class ImageEntity extends AuditableEntity {
    private String imageUrl;
    private String publicId;
    private String caption;
    private Boolean isDefault = false;
    private Integer sortOrder = 0;
}
