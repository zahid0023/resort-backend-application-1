package com.example.resortbackendapplication1.bookingsource.model.entity;

import com.example.resortbackendapplication1.commons.model.entity.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "booking_sources")
public class BookingSourceEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "bookingSourceEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingSourceLocaleEntity> bookingSourceLocaleEntities = new LinkedHashSet<>();

    public void addBookingSourceLocaleEntity(BookingSourceLocaleEntity entity) {
        addChild(bookingSourceLocaleEntities, entity, BookingSourceLocaleEntity::assignBookingSource, this);
    }

    public void removeBookingSourceLocaleEntity(BookingSourceLocaleEntity entity) {
        removeChild(bookingSourceLocaleEntities, entity, (child, ignored) -> child.unassignBookingSource());
    }
}
