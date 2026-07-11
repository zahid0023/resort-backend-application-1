package com.example.resortbackendapplication1.contact.dto.response;

import com.example.resortbackendapplication1.contact.model.dto.ContactTypeDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactTypeResponse {
    private final ContactTypeDto data;

    public ContactTypeResponse(ContactTypeDto contactType) {
        this.data = contactType;
    }
}
