package com.example.resortbackendapplication1.contact.dto.response;

import com.example.resortbackendapplication1.contact.model.dto.CommunicationChannelDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunicationChannelResponse {
    private final CommunicationChannelDto data;

    public CommunicationChannelResponse(CommunicationChannelDto communicationChannel) {
        this.data = communicationChannel;
    }
}
