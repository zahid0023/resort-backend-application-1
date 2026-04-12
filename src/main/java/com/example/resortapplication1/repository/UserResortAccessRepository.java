package com.example.resortapplication1.repository;

import com.example.resortapplication1.model.entity.UserResortAccessEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResortAccessRepository extends JpaRepository<@NonNull UserResortAccessEntity, @NonNull Long> {
}
