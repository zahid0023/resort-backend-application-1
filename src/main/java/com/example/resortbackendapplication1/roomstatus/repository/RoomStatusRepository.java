package com.example.resortbackendapplication1.roomstatus.repository;

import com.example.resortbackendapplication1.roomstatus.model.entity.RoomStatusEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@SuppressWarnings("unused")
public interface RoomStatusRepository extends
        JpaRepository<@NonNull RoomStatusEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull RoomStatusEntity> {

    Optional<RoomStatusEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByCodeAndIsActiveAndIsDeleted(String code, Boolean isActive, Boolean isDeleted);

}
