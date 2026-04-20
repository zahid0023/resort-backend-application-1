package com.example.resortbackendapplication1.commons.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageItemRequest {
    private MultipartFile image;
    private String caption;
    private Boolean isDefault = false;
    private Integer sortOrder = 0;
}
