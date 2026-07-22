package com.example.resortbackendapplication1.resortroomcategoryprice.repository;

import com.example.resortbackendapplication1.resortroomcategoryprice.model.entity.ResortRoomCategoryPriceDayEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResortRoomCategoryPriceDayRepository extends
        JpaRepository<@NonNull ResortRoomCategoryPriceDayEntity, @NonNull Long> {
}
