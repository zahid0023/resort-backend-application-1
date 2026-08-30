package com.example.resortbackendapplication1.contact.dto.response.userphones;

import com.example.resortbackendapplication1.contact.model.dto.UserPhoneDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPhoneResponse {

    private final UserPhoneDto data;

    public UserPhoneResponse(UserPhoneDto data) {
        this.data = data;
    }
}
