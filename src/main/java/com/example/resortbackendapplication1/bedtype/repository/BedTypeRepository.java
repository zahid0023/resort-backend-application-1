package com.example.resortbackendapplication1.bedtype.repository;

import com.example.resortbackendapplication1.bedtype.model.entity.BedTypeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BedTypeRepository extends JpaRepository<@NonNull BedTypeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull BedTypeEntity> {

    Optional<BedTypeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<BedTypeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);
}
