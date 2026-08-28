package com.example.resortbackendapplication1.resort.room.model.dto;
import com.example.resortbackendapplication1.resort.roomcategory.model.dto.ResortRoomCategoryDto;

import com.example.resortbackendapplication1.roomstatus.model.dto.RoomStatusDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortRoomDto {

    private Long id;

    private String code;

    private Integer sortOrder;

    private Integer floorNumber;

    private String building;

    private ResortRoomCategoryDto resortRoomCategory;

    private RoomStatusDto roomStatus;

    private ResortRoomLocaleDto locale;

    private ResortRoomMetaDto meta;

    private List<ResortRoomBedDto> beds;
}
