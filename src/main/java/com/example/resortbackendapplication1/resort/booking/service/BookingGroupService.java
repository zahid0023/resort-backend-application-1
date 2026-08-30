package com.example.resortbackendapplication1.resort.booking.service;

import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.resort.booking.dto.response.bookinggroups.BookingGroupResponse;
import com.example.resortbackendapplication1.resort.booking.model.entity.BookingGroupEntity;
import com.example.resortbackendapplication1.resort.core.model.entity.ResortEntity;

public interface BookingGroupService {

    /** Every booking — even a single-room one, a "group of one" — starts here. */
    SuccessResponse create(ResortEntity resortEntity, UserEntity customerEntity);

    BookingGroupEntity getEntityById(Long resortId, Long id);

    /** Embeds every active reservation tagged with this group, each with its full nested detail. */
    BookingGroupResponse getById(Long resortId, Long id);
}
