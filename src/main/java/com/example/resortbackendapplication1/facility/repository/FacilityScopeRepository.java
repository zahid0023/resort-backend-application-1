package com.example.resortbackendapplication1.facility.repository;

import com.example.resortbackendapplication1.facility.model.entity.FacilityScopeEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public interface FacilityScopeRepository extends
        JpaRepository<@NonNull FacilityScopeEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull FacilityScopeEntity> {

    Optional<FacilityScopeEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<@NonNull FacilityScopeEntity> findAllByIdInAndIsActiveAndIsDeleted(Set<Long> ids, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    List<String> findCodeByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);

}
