package com.example.resortbackendapplication1.resort.core.repository;

import com.example.resortbackendapplication1.resort.core.model.entity.ResortUserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortUserRepository extends JpaRepository<@NonNull ResortUserEntity, @NonNull Long> {
}
