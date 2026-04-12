package com.example.resortbackendapplication1.repository;

import com.example.resortbackendapplication1.model.entity.UserResortAccessEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResortAccessRepository extends JpaRepository<@NonNull UserResortAccessEntity, @NonNull Long> {
}
