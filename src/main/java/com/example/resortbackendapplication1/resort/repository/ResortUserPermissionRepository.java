package com.example.resortbackendapplication1.resort.repository;

import com.example.resortbackendapplication1.resort.model.entity.ResortUserPermissionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortUserPermissionRepository extends JpaRepository<@NonNull ResortUserPermissionEntity, @NonNull Long> {
}
