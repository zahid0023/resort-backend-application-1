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
@Table(name = "payment_methods")
public class PaymentMethodEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "paymentMethodEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentMethodLocaleEntity> paymentMethodLocaleEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PaymentMethod Locale relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentMethodLocaleEntity(PaymentMethodLocaleEntity entity) {
        addChild(paymentMethodLocaleEntities, entity, PaymentMethodLocaleEntity::assignPaymentMethod, this);
    }

    public void removePaymentMethodLocaleEntity(PaymentMethodLocaleEntity entity) {
        removeChild(paymentMethodLocaleEntities, entity, (child, ignored) -> child.unassignPaymentMethod());
    }

    @OneToMany(mappedBy = "paymentMethodEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentProviderEntity> paymentProviderEntities = new LinkedHashSet<>();

    // -------------------------------------------------------------------------
    // PaymentProvider relationship helpers
    // -------------------------------------------------------------------------

    public void addPaymentProviderEntity(PaymentProviderEntity entity) {
        addChild(paymentProviderEntities, entity, PaymentProviderEntity::assignPaymentMethod, this);
    }

    public void removePaymentProviderEntity(PaymentProviderEntity entity) {
        removeChild(paymentProviderEntities, entity, (child, ignored) -> child.unassignPaymentMethod());
    }
}
