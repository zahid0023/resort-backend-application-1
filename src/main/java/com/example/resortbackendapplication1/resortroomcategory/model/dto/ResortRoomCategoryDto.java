package com.example.resortbackendapplication1.resortroomcategory.model.dto;

import com.example.resortbackendapplication1.roomcategory.model.dto.RoomCategoryDto;
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
public class ResortRoomCategoryDto {
    private Long id;
    private Long resortId;
    private RoomCategoryDto roomCategory;
    private String code;
    private Integer sortOrder;
    private ResortRoomCategoryMetaDto meta;
    private List<ResortRoomCategoryBedDto> beds;
    private List<ResortRoomCategoryLocaleDto> locales;
}
