package com.example.resortbackendapplication1.resort.roomcategory.repository;

import com.example.resortbackendapplication1.resort.roomcategory.model.entity.ResortRoomCategoryEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ResortRoomCategoryRepository extends
        JpaRepository<@NonNull ResortRoomCategoryEntity, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull ResortRoomCategoryEntity> {

    Optional<ResortRoomCategoryEntity> findByResortEntity_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndCodeAndIsActiveAndIsDeleted(
            Long resortId, String code, Boolean isActive, Boolean isDeleted);

    boolean existsByResortEntity_IdAndRoomCategoryEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Long roomCategoryId, Boolean isActive, Boolean isDeleted);

    /**
     * The platform {@code RoomCategory} codes this resort already has an active
     * {@code ResortRoomCategory} for — matched via {@code room_category_id}, never via this entity's own
     * (independently settable) {@code code} column.
     */
    @Query("select rrc.roomCategoryEntity.code from ResortRoomCategoryEntity rrc "
            + "where rrc.resortEntity.id = :resortId and rrc.isActive = :isActive and rrc.isDeleted = :isDeleted")
    List<String> findRoomCategoryCodeByResortEntity_IdAndIsActiveAndIsDeleted(
            Long resortId, Boolean isActive, Boolean isDeleted);
}
