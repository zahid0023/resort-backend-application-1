package com.example.resortbackendapplication1.imagehosting.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.imagehosting.enums.ImageHostingProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "resort_image_hosting_configs")
public class ResortImageHostingConfigEntity extends AuditableEntity {

    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 50)
    private ImageHostingProvider provider;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> config;
}
