package com.example.resortbackendapplication1.contact.repository;

import com.example.resortbackendapplication1.contact.model.entity.ContactTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContactTypeRepository extends JpaRepository<@NonNull ContactTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ContactTypeEntity> {

    Optional<ContactTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<ContactTypeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    Optional<ContactTypeEntity> findByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);
}
