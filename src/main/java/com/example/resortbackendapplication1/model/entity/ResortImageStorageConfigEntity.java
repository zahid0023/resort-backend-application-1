package com.example.resortbackendapplication1.model.entity;

import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
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
@Table(name = "resort_image_storage_configs")
public class ResortImageStorageConfigEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    @NotNull
    @Column(name = "provider", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ImageHostingProvider provider;

    @NotNull
    @Column(name = "config", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> config;
}