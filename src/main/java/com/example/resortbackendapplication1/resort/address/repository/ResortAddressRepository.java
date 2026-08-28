package com.example.resortbackendapplication1.resort.address.repository;

import com.example.resortbackendapplication1.resort.address.model.entity.ResortAddressEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortAddressRepository extends JpaRepository<@NonNull ResortAddressEntity, @NonNull Long> {

    Optional<ResortAddressEntity> findByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted);

}
