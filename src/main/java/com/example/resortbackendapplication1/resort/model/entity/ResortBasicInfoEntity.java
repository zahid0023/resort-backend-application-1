package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resort_basic_info")
public class ResortBasicInfoEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "resort_id", nullable = false, unique = true)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#assignResortBasicInfoEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#unassignResortBasicInfoEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @NotNull
    @Column(name = "estd", nullable = false)
    private Short estd;

    @Column(name = "logo_url", length = Integer.MAX_VALUE)
    private String logoUrl;

    @OneToMany(mappedBy = "resortBasicInfoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortBasicInfoLocaleEntity> resortBasicInfoLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortBasicInfoLocale relationship helpers
    // -------------------------------------------------------------------------

    public void addResortBasicInfoLocaleEntity(ResortBasicInfoLocaleEntity entity) {
        addChild(resortBasicInfoLocaleEntities, entity, ResortBasicInfoLocaleEntity::assignResortBasicInfo, this);
    }

    public void removeResortBasicInfoLocaleEntity(ResortBasicInfoLocaleEntity entity) {
        removeChild(resortBasicInfoLocaleEntities, entity, (child, ignored) -> child.unassignResortBasicInfo());
    }
}
