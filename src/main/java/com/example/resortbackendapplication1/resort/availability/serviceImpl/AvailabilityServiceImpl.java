package com.example.resortbackendapplication1.resort.availability.serviceImpl;

import com.example.resortbackendapplication1.resort.availability.dto.request.availability.AvailabilityRequest;
import com.example.resortbackendapplication1.resort.availability.service.AvailabilityService;
import com.example.resortbackendapplication1.resort.roomreservation.repository.ResortRoomReservationRepository;
import com.example.resortbackendapplication1.resort.room.model.entity.ResortRoomEntity;
import com.example.resortbackendapplication1.resort.room.repository.ResortRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private static final String AVAILABLE_ROOM_STATUS_CODE = "AVAILABLE";

    private final ResortRoomRepository resortRoomRepository;
    private final ResortRoomReservationRepository resortRoomReservationRepository;

    public AvailabilityServiceImpl(ResortRoomRepository resortRoomRepository,
                                   ResortRoomReservationRepository resortRoomReservationRepository) {
        this.resortRoomRepository = resortRoomRepository;
        this.resortRoomReservationRepository = resortRoomReservationRepository;
    }

    @Override
    public List<ResortRoomEntity> search(Long resortId, AvailabilityRequest request) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new IllegalArgumentException("check_out must be after check_in");
        }

        List<ResortRoomEntity> rooms = resortRoomRepository
                .findByResortRoomCategoryEntity_ResortEntity_IdAndIsActiveAndIsDeleted(resortId, true, false);

        Set<Long> roomIds = rooms.stream().map(ResortRoomEntity::getId).collect(Collectors.toSet());
        Set<Long> occupiedRoomIds = roomIds.isEmpty()
                ? Set.of()
                : resortRoomReservationRepository.findOccupiedResortRoomIds(roomIds, request.getCheckIn(), request.getCheckOut());

        return rooms.stream().filter(room -> isAvailable(room, occupiedRoomIds)).toList();
    }

    private boolean isAvailable(ResortRoomEntity room, Set<Long> occupiedRoomIds) {
        return AVAILABLE_ROOM_STATUS_CODE.equals(room.getRoomStatusEntity().getCode())
                && !occupiedRoomIds.contains(room.getId());
    }
}
