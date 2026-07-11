package com.example.resortbackendapplication1.resortcontact.model.dto;

import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResortContactDto {
    private Long id;
    private Long resortId;
    private ContactTypeDto contactType;
    private CommunicationChannelDto communicationChannel;
    private String contactValue;
    private Boolean isPrimary;
    private Integer sortOrder;
}
