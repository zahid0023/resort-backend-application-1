package com.example.resortbackendapplication1.contact.dto.response.useremails;

import com.example.resortbackendapplication1.contact.model.dto.UserEmailDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserEmailResponse {

    private final UserEmailDto data;

    public UserEmailResponse(UserEmailDto data) {
        this.data = data;
    }
}
