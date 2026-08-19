package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resortroletype.model.entity.ResortRoleTypeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "resort_users")
public class ResortUserEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resortEntity;

    /** Internal — call via {@link ResortEntity#addResortUserEntity}. */
    public void assignResort(ResortEntity resortEntity) {
        this.resortEntity = resortEntity;
    }

    /** Internal — call via {@link ResortEntity#removeResortUserEntity}. */
    public void unassignResort() {
        this.resortEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    // Default role for this membership; explicit overrides live in resort_user_permissions.
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_role_type_id", nullable = false)
    private ResortRoleTypeEntity resortRoleTypeEntity;

    @NotNull
    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @OneToMany(mappedBy = "resortUserEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResortUserPermissionEntity> resortUserPermissionEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // ResortUserPermission relationship helpers
    // -------------------------------------------------------------------------

    public void addResortUserPermissionEntity(ResortUserPermissionEntity entity) {
        addChild(resortUserPermissionEntities, entity, ResortUserPermissionEntity::assignResortUser, this);
    }

    public void removeResortUserPermissionEntity(ResortUserPermissionEntity entity) {
        removeChild(resortUserPermissionEntities, entity, (child, ignored) -> child.unassignResortUser());
    }
}
