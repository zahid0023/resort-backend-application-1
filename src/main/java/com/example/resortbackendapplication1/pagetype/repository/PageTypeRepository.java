package com.example.resortbackendapplication1.pagetype.repository;

import com.example.resortbackendapplication1.pagetype.model.entity.PageTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PageTypeRepository extends JpaRepository<@NonNull PageTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull PageTypeEntity> {

    Optional<PageTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<PageTypeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
