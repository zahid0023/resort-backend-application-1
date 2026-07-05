package com.example.resortbackendapplication1.resort.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import com.example.resortbackendapplication1.resortpermissiontype.model.entity.ResortPermissionTypeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resort_user_permissions")
public class ResortUserPermissionEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_user_id", nullable = false)
    private ResortUserEntity resortUserEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resort_permission_type_id", nullable = false)
    private ResortPermissionTypeEntity resortPermissionTypeEntity;

    @NotNull
    @Column(name = "is_allowed", nullable = false)
    private Boolean isAllowed;
}
