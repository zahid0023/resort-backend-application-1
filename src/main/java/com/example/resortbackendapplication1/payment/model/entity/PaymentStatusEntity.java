package com.example.resortbackendapplication1.payment.model.entity;

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

import static com.example.resortbackendapplication1.commons.model.entity.EntityRelationshipHelper.*;

@Getter
@Setter
@Entity
@Table(name = "payment_statuses")
public class PaymentStatusEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "paymentStatusEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentStatusLocaleEntity> paymentStatusLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PaymentStatus Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentStatusLocaleEntity(PaymentStatusLocaleEntity entity) {
        addChild(paymentStatusLocaleEntities, entity, PaymentStatusLocaleEntity::assignPaymentStatus, this);
    }

    public void removePaymentStatusLocaleEntity(PaymentStatusLocaleEntity entity) {
        removeChild(paymentStatusLocaleEntities, entity, (child, ignored) -> child.unassignPaymentStatus());
    }
}
