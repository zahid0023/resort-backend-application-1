package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.ContactTypeLocaleEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactTypeLocaleRepository extends JpaRepository<@NonNull ContactTypeLocaleEntity, @NonNull Long> {

    Optional<ContactTypeLocaleEntity> findByContactTypeEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long contactTypeId, Long id, Boolean isActive, Boolean isDeleted);
}
