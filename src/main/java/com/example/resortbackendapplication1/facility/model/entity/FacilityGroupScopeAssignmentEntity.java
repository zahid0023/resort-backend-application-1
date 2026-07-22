package com.example.resortbackendapplication1.facility.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "facility_group_scope_assignments")
public class FacilityGroupScopeAssignmentEntity {

    @EmbeddedId
    private FacilityGroupScopeAssignmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("facilityGroupId")
    @JoinColumn(name = "facility_group_id")
    private FacilityGroupEntity facilityGroupEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("facilityScopeId")
    @JoinColumn(name = "facility_scope_id")
    private FacilityScopeEntity facilityScopeEntity;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @PrePersist
    protected void onCreate() {
        if (createdBy == null) createdBy = 1L;
        if (updatedBy == null) updatedBy = createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        if (updatedBy == null) updatedBy = createdBy;
    }
}
