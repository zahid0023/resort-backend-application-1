package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRepository extends
        JpaRepository<@NonNull ResortEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortEntity> {

    Optional<ResortEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

    @Query("""
            select r from ResortEntity r
            join r.resortUserEntities ru
            where ru.userEntity.id = :authenticatedUserId
              and ru.isActive = true and ru.isDeleted = false
              and r.isActive = true and r.isDeleted = false
            """)
    Page<ResortEntity> findAllByMemberUserId(@Param("authenticatedUserId") Long authenticatedUserId, Pageable pageable);

}
