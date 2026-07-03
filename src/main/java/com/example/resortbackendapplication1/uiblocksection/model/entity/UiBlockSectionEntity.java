package com.example.resortbackendapplication1.uiblocksection.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
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
@Table(name = "ui_block_sections")
public class UiBlockSectionEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @OneToMany(mappedBy = "uiBlockSectionEntity", cascade = CascadeType.ALL)
    private Set<UiBlockSectionLocaleEntity> uiBlockSectionLocaleEntities = new LinkedHashSet<>();
}
