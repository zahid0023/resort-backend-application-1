package com.example.resortbackendapplication1.resort.core.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "resort_user_permissions")
public class ResortUserPermissionEntity extends AuditableEntity {

    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resort_user_id", nullable = false)
    private ResortUserEntity resortUserEntity;

    /** Internal — call via {@link ResortUserEntity#addResortUserPermissionEntity}. */
    public void assignResortUser(ResortUserEntity resortUserEntity) {
        this.resortUserEntity = resortUserEntity;
    }

    /** Internal — call via {@link ResortUserEntity#removeResortUserPermissionEntity}. */
    public void unassignResortUser() {
        this.resortUserEntity = null;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_permission_type_id", nullable = false)
    private ResortPermissionTypeEntity resortPermissionTypeEntity;

    // true = explicitly allow
    // false = explicitly deny
    @NotNull
    @Column(name = "is_allowed", nullable = false)
    private Boolean isAllowed;
}
